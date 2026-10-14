/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that use observability.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
class ObservabilityProjectGenerationConfiguration {

	@Bean
	ObservabilityActuatorBuildCustomizer observabilityActuatorBuildCustomizer() {
		return new ObservabilityActuatorBuildCustomizer();
	}

	@Bean
	ObservabilityDistributedTracingBuildCustomizer observabilityDistributedTracingBuildCustomizer() {
		return new ObservabilityDistributedTracingBuildCustomizer();
	}

	@Bean
	ObservabilityHelpDocumentCustomizer observabilityHelpDocumentCustomizer(ProjectDescription description,
			Build build) {
		return new ObservabilityHelpDocumentCustomizer(description, build);
	}

}
