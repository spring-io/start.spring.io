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

package io.spring.start.site.extension.dependency.springkafka;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring Kafka and Kafka Streams.
 *
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
class SpringKafkaProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CONFLUENT_CLASS_NAME = "org.testcontainers.containers.KafkaContainer";

	private static final String TESTCONTAINERS_APACHE_CLASS_NAME = "org.testcontainers.kafka.KafkaContainer";

	private static final VersionRange SPRING_BOOT_3_4_M2_OR_LATER = VersionParser.DEFAULT.parseRange("3.4.0-M2");

	@Bean
	@ConditionalOnRequestedDependency("kafka")
	SpringKafkaBuildCustomizer springKafkaBuildCustomizer() {
		return new SpringKafkaBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer kafkaServiceConnectionsCustomizer(ProjectDescription description, Build build,
			DockerServiceResolver serviceResolver) {
		boolean isSpringBoot34OrLater = SPRING_BOOT_3_4_M2_OR_LATER.match(description.getPlatformVersion());
		return (serviceConnections) -> {
			if (isKafkaEnabled(build)) {
				if (isSpringBoot34OrLater) {
					serviceResolver.doWith("kafka-native", (service) -> serviceConnections.addServiceConnection(
							ServiceConnection.ofContainer("kafka", service, TESTCONTAINERS_APACHE_CLASS_NAME, false)));
				}
				else {
					serviceResolver.doWith("kafka",
							(service) -> serviceConnections.addServiceConnection(ServiceConnection.ofContainer("kafka",
									service, TESTCONTAINERS_CONFLUENT_CLASS_NAME, false)));
				}
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("kafka-streams")
	@ConditionalOnPlatformVersion("4.0.0-M1")
	SpringKafkaStreamsBuildCustomizer springKafkaStreamsBuildCustomizer() {
		return new SpringKafkaStreamsBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("kafka-streams")
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	SpringKafkaStreamsMavenBuildCustomizer springKafkaStreamsMavenBuildCustomizer(ProjectDescription description) {
		return new SpringKafkaStreamsMavenBuildCustomizer(description);
	}

	@Bean
	@ConditionalOnRequestedDependency("kafka-streams")
	@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
	SpringKafkaStreamsGradleBuildCustomizer springKafkaStreamsGradleBuildCustomizer(ProjectDescription description) {
		return new SpringKafkaStreamsGradleBuildCustomizer(description, '\'');
	}

	@Bean
	@ConditionalOnRequestedDependency("kafka-streams")
	@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
	SpringKafkaStreamsGradleBuildCustomizer springKafkaStreamsGradleKotlinBuildCustomizer(
			ProjectDescription description) {
		return new SpringKafkaStreamsGradleBuildCustomizer(description, '\"');
	}

	private boolean isKafkaEnabled(Build build) {
		return build.dependencies().has("kafka") || build.dependencies().has("kafka-streams");
	}

}
