/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.oracle;

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
 * Configuration for generation of projects that depend on Oracle.
 *
 * @author Moritz Halbritter
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
@Configuration(proxyBeanMethods = false)
class OracleProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer oracleServiceConnectionsCustomizer(Build build, DockerServiceResolver serviceResolver,
			Testcontainers testcontainers) {
		return (serviceConnections) -> {
			if (isOracleEnabled(build)) {
				Container container = testcontainers.getContainer(SupportedContainer.ORACLE);
				serviceResolver.doWith("oracleFree",
						(service) -> serviceConnections.addServiceConnection(ServiceConnection.ofContainer("oracleFree",
								service, container.className(), container.generic())));
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer oracleComposeFileCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (composeFile) -> {
			if (isOracleEnabled(build)) {
				serviceResolver.doWith("oracleFree", (service) -> composeFile.services()
					.add("oracle", service.andThen((builder) -> builder.environment("ORACLE_PASSWORD", "secret"))));
			}
		};
	}

	private boolean isOracleEnabled(Build build) {
		return build.dependencies().has("oracle") || build.dependencies().has("spring-ai-vectordb-oracle");
	}

}
