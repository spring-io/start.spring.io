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

package io.spring.start.site.extension.dependency.activemq;

import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on ActiveMQ.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("activemq")
public class ActiveMQProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CLASS_NAME = "org.testcontainers.activemq.ActiveMQContainer";

	@Bean
	@ConditionalOnPlatformVersion("[3.2.0-M1,3.3.0-M2)")
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer activeMQServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver.doWith("activeMQ", (service) -> serviceConnections
			.addServiceConnection(ServiceConnection.ofGenericContainer("activeMQ", service, "symptoma/activemq")));
	}

	@Bean
	@ConditionalOnPlatformVersion("3.3.0-M2")
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer activeMQClassicServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver.doWith("activeMQClassic",
				(service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("activemq", service, TESTCONTAINERS_CLASS_NAME, false)));
	}

	@Bean
	@ConditionalOnPlatformVersion("[3.2.0-M1,3.3.0-M2)")
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer activeMQComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("activeMQ",
				(service) -> composeFile.services().add("activemq", service));
	}

	@Bean
	@ConditionalOnPlatformVersion("3.3.0-M2")
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer activeMQClassicComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("activeMQClassic",
				(service) -> composeFile.services().add("activemq", service));
	}

}
