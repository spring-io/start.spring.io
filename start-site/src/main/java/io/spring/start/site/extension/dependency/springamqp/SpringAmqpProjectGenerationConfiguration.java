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

package io.spring.start.site.extension.dependency.springamqp;

import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
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
	@ConditionalOnRequestedDependency("amqp")
	static class AmqpConfiguration {

		@Bean
		@ConditionalOnPlatformVersion("[3.5.0,4.0.0-RC1]")
		SpringRabbitTestBuildCustomizer springAmqpTestBuildCustomizer() {
			return new SpringRabbitTestBuildCustomizer();
		}

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

	@ConditionalOnRequestedDependency("amqp-streams")
	@Configuration(proxyBeanMethods = false)
	static class AmqpStreamsConfiguration {

		@Bean
		SpringRabbitStreamsBuildCustomizer springRabbitStreamsBuildCustomizer() {
			return new SpringRabbitStreamsBuildCustomizer();
		}

	}

}
