/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependencyBuildCustomizer;
import io.spring.start.site.support.implicit.ImplicitDependencyHelpDocumentCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for customizations relevant to selected
 * dependencies.
 *
 * @author Yonghui Ye
 * @author Andy Wilkinson
 */
@ProjectGenerationConfiguration
class SpringAzureProjectGenerationConfiguration {

	private final Iterable<ImplicitDependency> azureDependencies;

	SpringAzureProjectGenerationConfiguration() {
		this.azureDependencies = SpringAzureModuleRegistry.createSpringBootRegistry();
	}

	@Bean
	ImplicitDependencyBuildCustomizer azureDependencyBuildCustomizer() {
		return new ImplicitDependencyBuildCustomizer(this.azureDependencies);
	}

	@Bean
	ImplicitDependencyHelpDocumentCustomizer azureDependencyHelpDocumentCustomizer(Build build) {
		return new ImplicitDependencyHelpDocumentCustomizer(this.azureDependencies, build);
	}

	@Bean
	@ConditionalOnRequestedDependency("azure-support")
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	SpringAzureMavenBuildCustomizer azureDependencyMavenBuildCustomizer(ProjectDescription projectDescription) {
		return new SpringAzureMavenBuildCustomizer(projectDescription);
	}

}
