/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.build.maven;

import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that depend on Maven.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnBuildSystem(MavenBuildSystem.ID)
class MavenProjectGenerationConfiguration {

	@Bean
	MavenBuildSystemHelpDocumentCustomizer mavenBuildSystemHelpDocumentCustomizer(ProjectDescription description) {
		return new MavenBuildSystemHelpDocumentCustomizer(description);
	}

}
