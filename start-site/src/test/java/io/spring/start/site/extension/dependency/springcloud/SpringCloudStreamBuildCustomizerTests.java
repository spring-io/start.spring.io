/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.springcloud;

import java.util.stream.Stream;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringCloudStreamBuildCustomizer}.
 *
 * @author Stephane Nicoll
 * @author Brian Clozel
 * @author Chris Bono
 */
class SpringCloudStreamBuildCustomizerTests extends AbstractExtensionTests {

	private static final Dependency KAFKA_BINDER = Dependency.withId("cloud-stream-binder-kafka",
			"org.springframework.cloud", "spring-cloud-stream-binder-kafka");

	private static final Dependency KAFKA_STREAMS_BINDER = Dependency.withId("cloud-stream-binder-kafka-streams",
			"org.springframework.cloud", "spring-cloud-stream-binder-kafka-streams");

	private static final Dependency PULSAR_BINDER = Dependency.withId("cloud-stream-binder-pulsar",
			"org.springframework.cloud", "spring-cloud-stream-binder-pulsar");

	private static final Dependency RABBIT_BINDER = Dependency.withId("cloud-stream-binder-rabbit",
			"org.springframework.cloud", "spring-cloud-stream-binder-rabbit");

	@ParameterizedTest
	@MethodSource("springCloudStreamArguments")
	void springCloudStreamWithRabbit(Version springBootVersion, Dependency testDependency) {
		ProjectRequest request = createProjectRequest("cloud-stream", "amqp");
		request.setBootVersion(springBootVersion.toString());
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-stream"))
			.hasDependency(getDependency("amqp"))
			.hasDependency(RABBIT_BINDER)
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependency(testDependency)
			.hasDependenciesSize(6);
	}

	@ParameterizedTest
	@MethodSource("springCloudStreamArguments")
	void springCloudStreamWithKafka(Version springBootVersion, Dependency testDependency) {
		ProjectRequest request = createProjectRequest("cloud-stream", "kafka");
		request.setBootVersion(springBootVersion.toString());
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-stream"))
			.hasDependency(getDependency("kafka"))
			.hasDependency(KAFKA_BINDER)
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependency(testDependency)
			.hasDependenciesSize(6);
	}

	@ParameterizedTest
	@MethodSource("springCloudStreamArguments")
	void springCloudStreamWithKafkaStreams(Version springBootVersion, Dependency testDependency) {
		ProjectRequest request = createProjectRequest("cloud-stream", "kafka-streams");
		request.setBootVersion(springBootVersion.toString());
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-stream"))
			.hasDependency(getDependency("kafka-streams"))
			.hasDependency(KAFKA_STREAMS_BINDER)
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependency(testDependency)
			.hasDependenciesSize(5);
	}

	@Test
	void springCloudStreamWithPulsar() {
		ProjectRequest request = createProjectRequest("cloud-stream", "pulsar");
		request.setBootVersion("3.2.0-RC1");
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-stream"))
			.hasDependency(getDependency("pulsar"))
			.hasDependency(PULSAR_BINDER)
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST));
	}

	@ParameterizedTest
	@MethodSource("springCloudStreamArguments")
	void springCloudStreamWithAllBinders(Version springBootVersion, Dependency testDependency) {
		ProjectRequest request = createProjectRequest("cloud-stream", "amqp", "kafka", "kafka-streams");
		request.setBootVersion(springBootVersion.toString());
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-stream"))
			.hasDependency(getDependency("amqp"))
			.hasDependency(getDependency("kafka"))
			.hasDependency(getDependency("kafka-streams"))
			.hasDependency(RABBIT_BINDER)
			.hasDependency(KAFKA_BINDER)
			.hasDependency(KAFKA_STREAMS_BINDER)
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependency(testDependency)
			.hasDependenciesSize(11);
	}

	@ParameterizedTest
	@MethodSource("springCloudStreamArguments")
	void springCloudBusWithRabbit(Version springBootVersion, Dependency testDependency) {
		ProjectRequest request = createProjectRequest("cloud-bus", "amqp");
		request.setBootVersion(springBootVersion.toString());
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-bus"))
			.hasDependency(getDependency("amqp"))
			.hasDependency(RABBIT_BINDER)
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependenciesSize(5);
	}

	@ParameterizedTest
	@MethodSource("springCloudStreamArguments")
	void springCloudBusWithKafka(Version springBootVersion, Dependency testDependency) {
		ProjectRequest request = createProjectRequest("cloud-bus", "amqp");
		request.setBootVersion(springBootVersion.toString());
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-bus"))
			.hasDependency(getDependency("amqp"))
			.hasDependency(RABBIT_BINDER)
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependenciesSize(5);
	}

	@ParameterizedTest
	@MethodSource("springCloudStreamArguments")
	void springCloudBusWithAllBinders(Version springBootVersion, Dependency testDependency) {
		ProjectRequest request = createProjectRequest("cloud-bus", "amqp", "kafka", "kafka-streams");
		request.setBootVersion(springBootVersion.toString());
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-bus"))
			.hasDependency(getDependency("amqp"))
			.hasDependency(getDependency("kafka"))
			.hasDependency(getDependency("kafka-streams"))
			.hasDependency(RABBIT_BINDER)
			.hasDependency(KAFKA_BINDER)
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependenciesSize(9);
	}

	@Test
	void springCloudStreamWithGradleBuildDoesNotAddTestDependency() {
		ProjectRequest request = createProjectRequest("cloud-stream", "amqp");
		request.setBootVersion("2.7.0");
		assertThat(gradleBuild(request)).doesNotContain("test-binder");
	}

	private static Stream<Arguments> springCloudStreamArguments() {
		Dependency scsTest = Dependency.withId("cloud-stream-test", "org.springframework.cloud", "spring-cloud-stream",
				null, Dependency.SCOPE_TEST);
		scsTest.setClassifier("test-binder");
		scsTest.setType("test-jar");
		Dependency testBinder = Dependency.withId("cloud-stream-test", "org.springframework.cloud",
				"spring-cloud-stream-test-binder", null, Dependency.SCOPE_TEST);
		return Stream.of(Arguments.of(Version.parse("2.7.0"), scsTest),
				Arguments.of(Version.parse("3.0.0"), testBinder));
	}

}
