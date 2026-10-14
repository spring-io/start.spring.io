/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springmodulith;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * Registers {@link SpringModulithBuildCustomizer} with the context to allow selecting
 * Spring Modulith dependencies.
 *
 * @author Oliver Drotbohm
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("modulith")
class SpringModulithProjectGenerationConfiguration {

	@Bean
	SpringModulithBuildCustomizer springModulithBuildCustomizer(ProjectDescription description) {
		return new SpringModulithBuildCustomizer(description.getPlatformVersion());
	}

}
