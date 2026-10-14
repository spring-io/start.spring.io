/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.h2;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on H2.
 *
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("h2")
class H2ProjectGenerationConfiguration {

	@Bean
	BuildCustomizer<Build> addH2ConsoleModuleBuildCustomizer() {
		return (build) -> build.dependencies()
			.add("spring-boot-h2console", "org.springframework.boot", "spring-boot-h2console", DependencyScope.COMPILE);
	}

}
