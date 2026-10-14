/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.derby;

import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Derby.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("derby")
class DerbyProjectGenerationConfiguration {

	@Bean
	BuildCustomizer<?> derbyToolsBuilderCustomizer() {
		return (build) -> build.dependencies()
			.add("derbytools", "org.apache.derby", "derbytools", DependencyScope.RUNTIME);
	}

}
