/*
 * Copyright 2012-2024 the original author or authors.
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

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring AMQP.
 *
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
class SpringAmqpProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CLASS_NAME = "org.testcontainers.containers.RabbitMQContainer";

	@Bean
	@ConditionalOnRequestedDependency("amqp")
	SpringRabbitTestBuildCustomizer springAmqpTestBuildCustomizer() {
		return new SpringRabbitTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer rabbitServiceConnectionsCustomizer(Build build,
			DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver.doWith("rabbit", (service) -> {
			if (isAmqpEnabled(build)) {
				serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("rabbit", service, TESTCONTAINERS_CLASS_NAME, false));
			}
		});
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer rabbitComposeFileCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("rabbit", (service) -> {
			if (isAmqpEnabled(build)) {
				composeFile.services()
					.add("rabbitmq",
							service.andThen((builder) -> builder.environment("RABBITMQ_DEFAULT_USER", "myuser")
								.environment("RABBITMQ_DEFAULT_PASS", "secret")
								.ports(5672)));
			}
		});
	}

	private boolean isAmqpEnabled(Build build) {
		return build.dependencies().has("amqp") || build.dependencies().has("amqp-streams");
	}

}
