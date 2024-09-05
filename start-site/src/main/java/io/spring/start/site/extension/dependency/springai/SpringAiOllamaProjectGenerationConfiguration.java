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

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Ollama.
 *
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("spring-ai-ollama")
class SpringAiOllamaProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CLASS_NAME = "org.testcontainers.ollama.OllamaContainer";

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer ollamaServiceConnectionsCustomizer(InitializrMetadata metadata,
			ProjectDescription description, DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> {
			if (SpringAiVersion.version1OrLater(metadata, description.getPlatformVersion())) {
				serviceResolver.doWith("ollama", (service) -> serviceConnections
					.addServiceConnection(ServiceConnection.ofContainer("ollama", service, TESTCONTAINERS_CLASS_NAME)));
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer ollamaComposeFileCustomizer(InitializrMetadata metadata, ProjectDescription description,
			DockerServiceResolver serviceResolver) {
		return (composeFile) -> {
			if (SpringAiVersion.version1OrLater(metadata, description.getPlatformVersion())) {
				serviceResolver.doWith("ollama", (service) -> composeFile.services().add("ollama", service));
			}
		};
	}

}
