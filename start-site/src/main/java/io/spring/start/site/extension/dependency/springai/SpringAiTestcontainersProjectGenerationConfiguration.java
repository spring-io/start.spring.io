/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring AI Testcontainers.
 *
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("testcontainers")
@ConditionalOnRequestedSpringAiDependency
class SpringAiTestcontainersProjectGenerationConfiguration {

	@Bean
	BuildCustomizer<Build> springAiTestcontainersBuildCustomizer() {
		return (build) -> build.dependencies()
			.add("spring-ai-testcontainers",
					Dependency.withCoordinates("org.springframework.ai", "spring-ai-spring-boot-testcontainers")
						.scope(DependencyScope.TEST_COMPILE));
	}

}
