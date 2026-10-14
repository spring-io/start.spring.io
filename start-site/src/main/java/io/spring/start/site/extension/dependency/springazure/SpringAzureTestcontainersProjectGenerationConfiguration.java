/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring Azure Testcontainers.
 *
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("azure-storage")
class SpringAzureTestcontainersProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	BuildCustomizer<Build> springAzureTestcontainersBuildCustomizer() {
		return (build) -> build.dependencies()
			.add("spring-azure-testcontainers",
					Dependency.withCoordinates("com.azure.spring", "spring-cloud-azure-testcontainers")
						.scope(DependencyScope.TEST_COMPILE));
	}

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer azureStorageServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver.doWith("azurite",
				(service) -> serviceConnections.addServiceConnection(ServiceConnections.ServiceConnection
					.ofGenericContainer("azurite", service, "azure-storage/azurite")));
	}

}
