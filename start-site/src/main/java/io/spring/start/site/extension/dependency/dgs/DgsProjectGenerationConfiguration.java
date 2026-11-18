/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
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
