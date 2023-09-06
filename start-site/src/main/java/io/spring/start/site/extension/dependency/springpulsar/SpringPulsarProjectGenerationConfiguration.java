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

package io.spring.start.site.extension.dependency.springpulsar;

import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;

import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Pulsar.
 *
 * @author Chris Bono
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("pulsar")
class SpringPulsarProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("cloud-stream")
	SpringPulsarBinderBuildCustomizer pulsarBinderBuildCustomizer(InitializrMetadata metadata,
			ProjectDescription description) {
		return new SpringPulsarBinderBuildCustomizer(metadata, description);
	}

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	@ConditionalOnPlatformVersion("3.2.0-SNAPSHOT")
	ServiceConnectionsCustomizer pulsarServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver.doWith("pulsar",
				(service) -> serviceConnections.addServiceConnection(ServiceConnections.ServiceConnection
					.ofContainer("pulsar", service, "org.testcontainers.containers.PulsarContainer", false)));
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer pulsarComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("pulsar",
				(service) -> composeFile.services().add("pulsar", service));
	}

}
