/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring AI.
 *
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedSpringAiDependency
class SpringAiProjectGenerationConfiguration {

	@Bean
	SpringAiAdvisorsVectorStoreBuildCustomizer springAiVectorStoreAdvisorsBuildCustomizer() {
		return new SpringAiAdvisorsVectorStoreBuildCustomizer();
	}

}
