/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that use the Netflix
 * DGS.
 *
 * @author Brian Clozel
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("netflix-dgs")
class DgsProjectGenerationConfiguration {

	@Bean
	DgsBuildCustomizer dgsBuildCustomizer() {
		return new DgsBuildCustomizer();
	}

}
