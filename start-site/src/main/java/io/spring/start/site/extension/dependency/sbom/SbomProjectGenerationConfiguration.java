/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.sbom;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that use SBOMs.
 *
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("sbom-cyclone-dx")
class SbomProjectGenerationConfiguration {

	@Bean
	SbomCycloneDxBuildCustomizer sbomBuildCustomizer() {
		return new SbomCycloneDxBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	SbomCycloneDxMavenBuildCustomizer sbomCycloneDxMavenBuildCustomizer() {
		return new SbomCycloneDxMavenBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	SbomCycloneDxGradleBuildCustomizer sbomCycloneDxGradleBuildCustomizer(ProjectDescription description) {
		return new SbomCycloneDxGradleBuildCustomizer(description);
	}

}
