/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springkafka;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import io.spring.start.site.container.Testcontainers;
import io.spring.start.site.container.Testcontainers.Container;
import io.spring.start.site.container.Testcontainers.SupportedContainer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring Kafka and Kafka Streams.
 *
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 * @author Moritz Halbritter
 * @author Venkata Naga Sai Srikanth Gollapudi
 */
@ProjectGenerationConfiguration
class SpringKafkaProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer kafkaServiceConnectionsCustomizer(ProjectDescription description, Build build,
			DockerServiceResolver serviceResolver, Testcontainers testcontainers) {
		return (serviceConnections) -> {
			if (isKafkaEnabled(build)) {
				Container container = testcontainers.getContainer(SupportedContainer.KAFKA);
				serviceResolver.doWith("kafka-native", (service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("kafka", service, container.className(), container.generic())));
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("kafka-streams")
	SpringKafkaStreamsBuildCustomizer springKafkaStreamsBuildCustomizer() {
		return new SpringKafkaStreamsBuildCustomizer();
	}

	private boolean isKafkaEnabled(Build build) {
		return build.dependencies().has("kafka") || build.dependencies().has("kafka-streams");
	}

}
