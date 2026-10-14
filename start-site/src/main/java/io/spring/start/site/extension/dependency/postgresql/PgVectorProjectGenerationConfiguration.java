/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.postgresql;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
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
 * Configuration for generation of projects that depend on PgVector.
 *
 * @author Eddú Meléndez
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("spring-ai-vectordb-pgvector")
class PgVectorProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer pgvectorServiceConnectionsCustomizer(DockerServiceResolver serviceResolver,
			Testcontainers testcontainers) {
		Container container = testcontainers.getContainer(SupportedContainer.POSTGRESQL);
		return (serviceConnections) -> serviceResolver
			.doWith("pgvector", (service) -> serviceConnections.addServiceConnection(
					ServiceConnection.ofContainer("pgvector", service, container.className(), container.generic())));
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer pgvectorComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("pgvector", (service) -> {
			composeFile.services().remove("postgres");
			composeFile.services()
				.add("pgvector",
						service.andThen((builder) -> builder.environment("POSTGRES_USER", "myuser")
							.environment("POSTGRES_DB", "mydatabase")
							.environment("POSTGRES_PASSWORD", "secret")
							.label("org.springframework.boot.service-connection", "postgres")));
		});
	}

}
