/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.start.site.extension.dependency.testcontainers;

import java.util.stream.Stream;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TestcontainersBuildCustomizer}.
 *
 * @author Maciej Walkowiak
 * @author Stephane Nicoll
 */
class TestcontainersBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void buildWithOnlyTestContainers() {
		assertThat(generateProject("testcontainers")).mavenBuild()
				.hasBom("org.testcontainers", "testcontainers-bom", "${testcontainers.version}")
				.hasDependency(getDependency("testcontainers"));
	}

	@ParameterizedTest
	@MethodSource("supportedEntries")
	void buildWithSupportedEntries(String springBootDependencyId, String testcontainersArtifactId) {
		assertThat(generateProject("testcontainers", springBootDependencyId)).mavenBuild()
				.hasBom("org.testcontainers", "testcontainers-bom", "${testcontainers.version}")
				.hasDependency(getDependency(springBootDependencyId))
				.hasDependency("org.testcontainers", testcontainersArtifactId, null, "test")
				.hasDependency(getDependency("testcontainers"));
	}

	private ProjectStructure generateProject(String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setType("maven-build");
		return generateProject(request);
	}

	static Stream<Arguments> supportedEntries() {
		return Stream.of(Arguments.arguments("amqp", "rabbitmq"), Arguments.arguments("data-cassandra", "cassandra"),
				Arguments.arguments("data-cassandra-reactive", "cassandra"),
				Arguments.arguments("data-couchbase", "couchbase"),
				Arguments.arguments("data-couchbase-reactive", "couchbase"),
				Arguments.arguments("data-elasticsearch", "elasticsearch"),
				Arguments.arguments("data-mongodb", "mongodb"), Arguments.arguments("data-mongodb-reactive", "mongodb"),
				Arguments.arguments("data-neo4j", "neo4j"), Arguments.arguments("data-r2dbc", "r2dbc"),
				Arguments.arguments("data-solr", "solr"), Arguments.arguments("db2", "db2"),
				Arguments.arguments("kafka", "kafka"), Arguments.arguments("kafka-streams", "kafka"),
				Arguments.arguments("mysql", "mysql"), Arguments.arguments("postgresql", "postgresql"),
				Arguments.arguments("oracle", "oracle-xe"), Arguments.arguments("sqlserver", "mssqlserver"));
	}

}
