/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.properties;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.properties.ApplicationProperties;
import io.spring.initializr.generator.spring.properties.ApplicationPropertiesCustomizer;

import org.springframework.util.StringUtils;

/**
 * {@link ApplicationPropertiesCustomizer} to add default application properties.
 *
 * @author Moritz Halbritter
 */
class DefaultApplicationPropertiesCustomizer implements ApplicationPropertiesCustomizer {

	private final ProjectDescription projectDescription;

	DefaultApplicationPropertiesCustomizer(ProjectDescription projectDescription) {
		this.projectDescription = projectDescription;
	}

	@Override
	public void customize(ApplicationProperties properties) {
		String name = this.projectDescription.getName();
		if (StringUtils.hasLength(name)) {
			properties.add("spring.application.name", name);
			return;
		}
		String artifactId = this.projectDescription.getArtifactId();
		if (StringUtils.hasLength(artifactId)) {
			properties.add("spring.application.name", artifactId);
		}
	}

}
