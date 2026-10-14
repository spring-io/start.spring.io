/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.sqlserver;

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
 * Configuration for generation of projects that depend on SQL Server.
 *
 * @author Moritz Halbritter
 * @author Stephane Nicoll
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("sqlserver")
class SqlServerProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer sqlServerServiceConnectionsCustomizer(DockerServiceResolver serviceResolver,
			Testcontainers testcontainers) {
		Container container = testcontainers.getContainer(SupportedContainer.MSSQL);
		return (serviceConnections) -> serviceResolver
			.doWith("sqlServer", (service) -> serviceConnections.addServiceConnection(
					ServiceConnection.ofContainer("sqlServer", service, container.className(), container.generic())));
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer sqlServerComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("sqlServer",
				(service) -> composeFile.services()
					.add("sqlserver",
							service.andThen((builder) -> builder.environment("MSSQL_PID", "express")
								.environment("MSSQL_SA_PASSWORD", "verYs3cret")
								.environment("ACCEPT_EULA", "yes"))));
	}

}
