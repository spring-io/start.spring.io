/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springintegration;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependencyBuildCustomizer;
import io.spring.start.site.support.implicit.ImplicitDependencyHelpDocumentCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring Integration.
 *
 * @author Stephane Nicoll
 * @author Artem Bilan
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("integration")
class SpringIntegrationProjectGenerationConfiguration {

	private final Iterable<ImplicitDependency> dependencies;

	SpringIntegrationProjectGenerationConfiguration(ProjectDescription projectDescription) {
		this.dependencies = SpringIntegrationModuleRegistry.create(projectDescription.getPlatformVersion());
	}

	@Bean
	ImplicitDependencyBuildCustomizer springIntegrationBuildCustomizer() {
		return new ImplicitDependencyBuildCustomizer(this.dependencies);
	}

	@Bean
	ImplicitDependencyHelpDocumentCustomizer springIntegrationHelpCustomizer(Build build) {
		return new ImplicitDependencyHelpDocumentCustomizer(this.dependencies, build);
	}

}
