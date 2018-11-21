/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.WebProjectRequest;
import org.junit.Test;

/**
 * Tests for {@link SpringCloudStreamBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
public class SpringCloudStreamBuildCustomizerTests extends AbstractExtensionTests {

	private static final Dependency KAFKA_BINDER = Dependency.withId(
			"cloud-stream-binder-kafka", "org.springframework.cloud",
			"spring-cloud-stream-binder-kafka");

	private static final Dependency KAFKA_STREAMS_BINDER = Dependency.withId(
			"cloud-stream-binder-kafka-streams", "org.springframework.cloud",
			"spring-cloud-stream-binder-kafka-streams");

	private static final Dependency RABBIT_BINDER = Dependency.withId(
			"cloud-stream-binder-rabbit", "org.springframework.cloud",
			"spring-cloud-stream-binder-rabbit");

	private static final Dependency SCS_TEST = Dependency.withId("cloud-stream-test",
			"org.springframework.cloud", "spring-cloud-stream-test-support", null,
			Dependency.SCOPE_TEST);

	@Test
	public void springCloudStreamWithRabbit() {
		WebProjectRequest request = createProjectRequest("cloud-stream", "amqp");
		generateMavenPom(request).hasDependency(getDependency("cloud-stream"))
				.hasDependency(getDependency("amqp")).hasDependency(RABBIT_BINDER)
				.hasSpringBootStarterTest().hasDependency(SCS_TEST)
				.hasDependenciesCount(5);
	}

	@Test
	public void springCloudStreamWithKafka() {
		WebProjectRequest request = createProjectRequest("cloud-stream", "kafka");
		generateMavenPom(request).hasDependency(getDependency("cloud-stream"))
				.hasDependency(getDependency("kafka")).hasDependency(KAFKA_BINDER)
				.hasSpringBootStarterTest().hasDependency(SCS_TEST)
				.hasDependenciesCount(6);
	}

	@Test
	public void springCloudStreamWithKafkaStreams() {
		WebProjectRequest request = createProjectRequest("cloud-stream", "kafka-streams");
		request.setBootVersion("2.0.0.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("cloud-stream"))
				.hasDependency(getDependency("kafka-streams"))
				.hasDependency(KAFKA_STREAMS_BINDER).hasSpringBootStarterTest()
				.hasDependency(SCS_TEST).hasDependenciesCount(5);
	}

	@Test
	public void springCloudStreamWithAllBinders() {
		WebProjectRequest request = createProjectRequest("cloud-stream", "amqp", "kafka",
				"kafka-streams");
		generateMavenPom(request).hasDependency(getDependency("cloud-stream"))
				.hasDependency(getDependency("amqp"))
				.hasDependency(getDependency("kafka"))
				.hasDependency(getDependency("kafka-streams"))
				.hasDependency(RABBIT_BINDER).hasDependency(KAFKA_BINDER)
				.hasDependency(KAFKA_STREAMS_BINDER).hasSpringBootStarterTest()
				.hasDependency(SCS_TEST).hasDependenciesCount(10);
	}

	@Test
	public void reactiveSpringCloudStreamWithRabbit() {
		WebProjectRequest request = createProjectRequest("reactive-cloud-stream", "amqp");
		request.setBootVersion("2.0.0.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("reactive-cloud-stream"))
				.hasDependency(getDependency("amqp")).hasDependency(RABBIT_BINDER)
				.hasSpringBootStarterTest().hasDependency(SCS_TEST)
				.hasDependenciesCount(5);
	}

	@Test
	public void reactiveSpringCloudStreamWithKafka() {
		WebProjectRequest request = createProjectRequest("reactive-cloud-stream", "kafka");
		request.setBootVersion("2.0.0.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("reactive-cloud-stream"))
				.hasDependency(getDependency("kafka")).hasDependency(KAFKA_BINDER)
				.hasSpringBootStarterTest().hasDependency(SCS_TEST)
				.hasDependenciesCount(6);
	}

	@Test
	public void reactiveSpringCloudStreamWithKafkaStreams() {
		WebProjectRequest request = createProjectRequest("reactive-cloud-stream",
				"kafka-streams");
		request.setBootVersion("2.0.0.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("reactive-cloud-stream"))
				.hasDependency(getDependency("kafka-streams"))
				.hasDependency(KAFKA_STREAMS_BINDER).hasSpringBootStarterTest()
				.hasDependency(SCS_TEST).hasDependenciesCount(5);
	}

	@Test
	public void reactiveSpringCloudStreamWithAllBinders() {
		WebProjectRequest request = createProjectRequest("reactive-cloud-stream", "amqp",
				"kafka", "kafka-streams");
		request.setBootVersion("2.0.0.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("reactive-cloud-stream"))
				.hasDependency(getDependency("amqp"))
				.hasDependency(getDependency("kafka"))
				.hasDependency(getDependency("kafka-streams"))
				.hasDependency(RABBIT_BINDER).hasDependency(KAFKA_BINDER)
				.hasDependency(KAFKA_STREAMS_BINDER).hasSpringBootStarterTest()
				.hasDependency(SCS_TEST).hasDependenciesCount(10);
	}

	@Test
	public void springCloudBusWithRabbit() {
		WebProjectRequest request = createProjectRequest("cloud-bus", "amqp");
		generateMavenPom(request).hasDependency(getDependency("cloud-bus"))
				.hasDependency(getDependency("amqp")).hasDependency(RABBIT_BINDER)
				.hasSpringBootStarterTest().hasDependenciesCount(4);
	}

	@Test
	public void springCloudBusWithKafka() {
		WebProjectRequest request = createProjectRequest("cloud-bus", "amqp");
		generateMavenPom(request).hasDependency(getDependency("cloud-bus"))
				.hasDependency(getDependency("amqp")).hasDependency(RABBIT_BINDER)
				.hasSpringBootStarterTest().hasDependenciesCount(4);
	}

	@Test
	public void springCloudBusWithAllBinders() {
		WebProjectRequest request = createProjectRequest("cloud-bus", "amqp", "kafka",
				"kafka-streams");
		generateMavenPom(request).hasDependency(getDependency("cloud-bus"))
				.hasDependency(getDependency("amqp"))
				.hasDependency(getDependency("kafka"))
				.hasDependency(getDependency("kafka-streams"))
				.hasDependency(RABBIT_BINDER).hasDependency(KAFKA_BINDER)
				.hasSpringBootStarterTest().hasDependenciesCount(8);
	}

	@Test
	public void springCloudTurbineStreamWithRabbit() {
		WebProjectRequest request = createProjectRequest("cloud-turbine-stream", "amqp");
		request.setBootVersion("2.0.0.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("cloud-turbine-stream"))
				.hasDependency(getDependency("amqp")).hasDependency(RABBIT_BINDER)
				.hasSpringBootStarterTest().hasDependenciesCount(4);
	}

	@Test
	public void springCloudTurbineStreamWithKafka() {
		WebProjectRequest request = createProjectRequest("cloud-turbine-stream", "kafka");
		request.setBootVersion("2.0.0.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("cloud-turbine-stream"))
				.hasDependency(getDependency("kafka")).hasDependency(KAFKA_BINDER)
				.hasSpringBootStarterTest().hasDependenciesCount(5);
	}

	@Test
	public void springCloudTurbineStreamWithAllBinders() {
		WebProjectRequest request = createProjectRequest("cloud-turbine-stream", "amqp",
				"kafka", "kafka-streams");
		request.setBootVersion("2.0.0.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("cloud-turbine-stream"))
				.hasDependency(getDependency("amqp"))
				.hasDependency(getDependency("kafka"))
				.hasDependency(getDependency("kafka-streams"))
				.hasDependency(RABBIT_BINDER).hasDependency(KAFKA_BINDER)
				.hasSpringBootStarterTest().hasDependenciesCount(8);
	}

}
