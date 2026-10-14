/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.opentelemetry;

import io.spring.initializr.generator.buildsystem.Build;
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
 * Configuration for generation of projects that depend on OpenTelemetry.
 *
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("opentelemetry")
public class OpenTelemetryProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer openTelemetryServiceConnectionsCustomizer(Build build,
			DockerServiceResolver serviceResolver, Testcontainers testcontainers) {
		return (serviceConnections) -> {
			Container container = testcontainers.getContainer(SupportedContainer.GRAFANA_LGTM);
			serviceResolver.doWith("grafanaLgtm", (service) -> serviceConnections.addServiceConnection(
					ServiceConnection.ofContainer("grafanaLgtm", service, container.className(), container.generic())));
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer openTelemetryComposeFileCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("grafanaLgtm",
				(service) -> composeFile.services().add("grafana-lgtm", service));
	}

}
