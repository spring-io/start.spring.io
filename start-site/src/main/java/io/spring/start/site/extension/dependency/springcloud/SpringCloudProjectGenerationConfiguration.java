/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.MavenVersionResolver;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that depend on Spring
 * Cloud.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
class SpringCloudProjectGenerationConfiguration {

	private final InitializrMetadata metadata;

	private final ProjectDescription description;

	SpringCloudProjectGenerationConfiguration(InitializrMetadata metadata, ProjectDescription description) {
		this.metadata = metadata;
		this.description = description;
	}

	@Bean
	SpringCloudFunctionBuildCustomizer springCloudFunctionBuildCustomizer() {
		return new SpringCloudFunctionBuildCustomizer(this.metadata, this.description);
	}

	@Bean
	SpringCloudStreamBuildCustomizer springCloudStreamBuildCustomizer() {
		return new SpringCloudStreamBuildCustomizer();
	}

	@Bean
	SpringCloudProjectVersionResolver springCloudProjectVersionResolver(MavenVersionResolver versionResolver) {
		return new SpringCloudProjectVersionResolver(this.metadata, versionResolver);
	}

	@Bean
	SpringCloudFunctionHelpDocumentCustomizer springCloudFunctionHelpDocumentCustomizer(Build build,
			MustacheTemplateRenderer templateRenderer, SpringCloudProjectVersionResolver versionResolver) {
		return new SpringCloudFunctionHelpDocumentCustomizer(build, this.description, templateRenderer,
				versionResolver);
	}

	@Bean
	SpringCloudCircuitBreakerBuildCustomizer springCloudCircuitBreakerBuildCustomizer() {
		return new SpringCloudCircuitBreakerBuildCustomizer(this.metadata, this.description);
	}

}
