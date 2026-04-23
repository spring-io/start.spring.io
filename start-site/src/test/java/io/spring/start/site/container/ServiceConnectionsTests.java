/*
 * Copyright 2012 - present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.container;

import io.spring.initializr.generator.language.ClassName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Tests for {@link ServiceConnections}.
 *
 * @author Kaique Vieira Soares
 */
class ServiceConnectionsTests {

	@Test
	void addServiceConnectionStoresAndSortsById() {
		ServiceConnections connections = new ServiceConnections();
		ServiceConnections.ServiceConnection conn1 = ServiceConnections.ServiceConnection.ofContainer("zeta", null,
				"com.example.Zeta", false);
		ServiceConnections.ServiceConnection conn2 = ServiceConnections.ServiceConnection.ofContainer("alpha", null,
				"com.example.Alpha", false);

		connections.addServiceConnection(conn1);
		connections.addServiceConnection(conn2);

		assertThat(connections.values()).containsExactly(conn2, conn1);
	}

	@Test
	void addServiceConnectionWithDuplicateIdThrowsException() {
		ServiceConnections connections = new ServiceConnections();
		ServiceConnections.ServiceConnection conn = ServiceConnections.ServiceConnection.ofContainer("test", null,
				"com.example.Test", false);

		connections.addServiceConnection(conn);

		assertThatIllegalArgumentException().isThrownBy(() -> connections.addServiceConnection(conn))
			.withMessageContaining("Connection with id 'test' already registered");
	}

	@Test
	void annotationRequestHandlesNullAttributes() {
		ServiceConnections.AnnotationRequest request = new ServiceConnections.AnnotationRequest(
				ClassName.of("org.example.Test"), null);
		assertThat(request.customizer()).isNotNull();
	}

	@Test
	void serviceConnectionHandlesNullAnnotations() {
		ServiceConnections.ServiceConnection conn = new ServiceConnections.ServiceConnection("test", null,
				"com.example.Test", false, null, null);
		assertThat(conn.annotations()).isEmpty();
	}

	@Test
	void ofGenericContainerCreatesExpectedInstance() {
		ServiceConnections.ServiceConnection conn = ServiceConnections.ServiceConnection.ofGenericContainer("redis",
				null, "redisConnection");

		assertThat(conn.id()).isEqualTo("redis");
		assertThat(conn.isGenericContainer()).isTrue();
		assertThat(conn.connectionName()).isEqualTo("redisConnection");
		assertThat(conn.annotations()).isEmpty();
	}

	@Test
	void ofContainerCreatesExpectedInstance() {
		ServiceConnections.ServiceConnection conn = ServiceConnections.ServiceConnection.ofContainer("mongo", null,
				"org.testcontainers.containers.MongoDBContainer", false);

		assertThat(conn.id()).isEqualTo("mongo");
		assertThat(conn.isGenericContainer()).isFalse();
		assertThat(conn.connectionName()).isNull();
		assertThat(conn.annotations()).isEmpty();
	}

	@Test
	void withAnnotationAddsSimpleAnnotationAndPreservesImmutability() {
		ServiceConnections.ServiceConnection original = ServiceConnections.ServiceConnection.ofContainer("test", null,
				"com.example.Test", false);

		ServiceConnections.ServiceConnection updated = original.withAnnotation(ClassName.of("org.example.Ssl"));

		assertThat(original.annotations()).isEmpty();
		assertThat(updated.annotations()).hasSize(1)
			.extracting(ServiceConnections.AnnotationRequest::className)
			.containsExactly(ClassName.of("org.example.Ssl"));
	}

	@Test
	void withAnnotationAddsAnnotationWithCustomizer() {
		ServiceConnections.ServiceConnection original = ServiceConnections.ServiceConnection.ofContainer("test", null,
				"com.example.Test", false);

		ServiceConnections.ServiceConnection updated = original.withAnnotation(ClassName.of("org.example.Ssl"),
				(builder) -> {
					builder.set("bundle", "mybundle");
					builder.set("enabled", true);
				});

		assertThat(updated.annotations()).hasSize(1)
			.extracting(ServiceConnections.AnnotationRequest::className)
			.containsExactly(ClassName.of("org.example.Ssl"));
	}

	@Test
	void withAnnotationChainingAddsMultipleAnnotations() {
		ServiceConnections.ServiceConnection conn = ServiceConnections.ServiceConnection
			.ofContainer("test", null, "com.example.Test", false)
			.withAnnotation(ClassName.of("org.example.First"))
			.withAnnotation(ClassName.of("org.example.Second"), (builder) -> builder.set("priority", 1));

		assertThat(conn.annotations()).hasSize(2)
			.extracting(ServiceConnections.AnnotationRequest::className)
			.containsExactly(ClassName.of("org.example.First"), ClassName.of("org.example.Second"));
	}

}
