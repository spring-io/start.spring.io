/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springamqp;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import io.spring.start.site.container.Testcontainers;
import io.spring.start.site.container.Testcontainers.Container;
import io.spring.start.site.container.Testcontainers.SupportedContainer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Spring AMQP.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
class SpringAmqpProjectGenerationConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnRequestedDependency("rabbitmq")
	static class RabbitMqConfiguration {

		@Bean
		@ConditionalOnRequestedDependency("testcontainers")
		ServiceConnectionsCustomizer rabbitServiceConnectionsCustomizer(DockerServiceResolver serviceResolver,
				Testcontainers testcontainers) {
			Container container = testcontainers.getContainer(SupportedContainer.RABBITMQ);
			return (serviceConnections) -> serviceResolver
				.doWith("rabbit", (service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("rabbit", service, container.className(), container.generic())));
		}

		@Bean
		@ConditionalOnRequestedDependency("docker-compose")
		ComposeFileCustomizer rabbitComposeFileCustomizer(DockerServiceResolver serviceResolver) {
			return (composeFile) -> serviceResolver.doWith("rabbit",
					(service) -> composeFile.services()
						.add("rabbitmq",
								service.andThen((builder) -> builder.environment("RABBITMQ_DEFAULT_USER", "myuser")
									.environment("RABBITMQ_DEFAULT_PASS", "secret")
									.ports(5672))));
		}

	}

	@ConditionalOnRequestedDependency("rabbitmq-streams")
	@Configuration(proxyBeanMethods = false)
	static class RabbitMqStreamsConfiguration {

		@Bean
		SpringRabbitStreamsBuildCustomizer springRabbitStreamsBuildCustomizer() {
			return new SpringRabbitStreamsBuildCustomizer();
		}

	}

}
