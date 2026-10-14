/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.properties;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for customizations relevant to the application
 * properties.
 *
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
class ApplicationPropertiesProjectGenerationConfiguration {

	@Bean
	DefaultApplicationPropertiesCustomizer defaultApplicationPropertiesContributorCustomizer(
			ProjectDescription projectDescription) {
		return new DefaultApplicationPropertiesCustomizer(projectDescription);
	}

}
