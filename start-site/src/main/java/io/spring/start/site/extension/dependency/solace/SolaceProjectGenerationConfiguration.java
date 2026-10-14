/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.solace;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Solace.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("solace")
class SolaceProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("cloud-stream")
	SolaceBinderBuildCustomizer solaceBinderBuildCustomizer(InitializrMetadata metadata,
			ProjectDescription description) {
		return new SolaceBinderBuildCustomizer(metadata, description);
	}

}
