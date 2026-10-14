/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springai;

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

/**
 * Configuration for generation of projects that depend on Ollama.
 *
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("spring-ai-ollama")
class SpringAiOllamaProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer ollamaServiceConnectionsCustomizer(DockerServiceResolver serviceResolver,
			Testcontainers testcontainers) {
		Container container = testcontainers.getContainer(SupportedContainer.OLLAMA);
		return (serviceConnections) -> serviceResolver.doWith("ollama",
				(service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("ollama", service, container.className(), container.generic())));
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer ollamaComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("ollama",
				(service) -> composeFile.services().add("ollama", service));
	}

}
