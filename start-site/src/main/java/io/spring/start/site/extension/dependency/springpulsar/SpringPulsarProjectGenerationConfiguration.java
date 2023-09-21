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

import java.util.Map;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.condition.ProjectGenerationCondition;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import io.spring.start.site.extension.dependency.springpulsar.SpringPulsarProjectGenerationConfiguration.OnPulsarDependencyCondition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Configuration for generation of projects that depend on Pulsar.
 *
 * @author Chris Bono
 */
@ProjectGenerationConfiguration
@Conditional(OnPulsarDependencyCondition.class)
class SpringPulsarProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CLASS_NAME = "org.testcontainers.containers.PulsarContainer";

	@Bean
	@ConditionalOnPlatformVersion("3.2.0-M3")
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer pulsarServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver.doWith("pulsar",
				(service) -> serviceConnections.addServiceConnection(ServiceConnections.ServiceConnection
					.ofContainer("pulsar", service, TESTCONTAINERS_CLASS_NAME, false)));
	}

	@Bean
	@ConditionalOnPlatformVersion("3.2.0-M3")
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer pulsarComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("pulsar",
				(service) -> composeFile.services().add("pulsar", service));
	}

	@Bean
	@ConditionalOnPlatformVersion("[3.0.0,3.2.0-M1)")
	@ConditionalOnRequestedDependency("cloud-stream")
	SpringPulsarBinderBuildCustomizer pulsarBinderBuildCustomizer(InitializrMetadata metadata,
			ProjectDescription description) {
		return new SpringPulsarBinderBuildCustomizer(metadata, description);
	}

	static class OnPulsarDependencyCondition extends ProjectGenerationCondition {

		@Override
		protected boolean matches(ProjectDescription description, ConditionContext context,
				AnnotatedTypeMetadata metadata) {
			Map<String, Dependency> requestedDependencies = description.getRequestedDependencies();
			return requestedDependencies.containsKey("pulsar") || requestedDependencies.containsKey("pulsar-reactive");
		}

	}

}
