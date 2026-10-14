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
 * Configuration for generation of projects that depend on Typesense.
 *
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("spring-ai-vectordb-typesense")
class SpringAiTypesenseProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer typesenseServiceConnectionsCustomizer(DockerServiceResolver serviceResolver,
			Testcontainers testcontainers) {
		Container container = testcontainers.getContainer(SupportedContainer.TYPESENSE);
		return (serviceConnections) -> serviceResolver
			.doWith("typesense", (service) -> serviceConnections.addServiceConnection(
					ServiceConnection.ofContainer("typesense", service, container.className(), container.generic())));
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer typesenseComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("typesense",
				(service) -> composeFile.services()
					.add("typesense", service.andThen((builder) -> builder.environment("TYPESENSE_API_KEY", "secret")
						.environment("TYPESENSE_DATA_DIR", "/tmp"))));
	}

}
