/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that depend on Spring
 * REST Docs.
 *
 * @author Andy Wilkinson
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("restdocs")
public class SpringRestDocsProjectGenerationConfiguration {

	@Bean
	SpringRestDocsBuildCustomizer springRestDocsBuildCustomizer() {
		return new SpringRestDocsBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
	SpringRestDocsGradleGroovyBuildCustomizer restDocsGradleGroovyBuildCustomizer() {
		return new SpringRestDocsGradleGroovyBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
	SpringRestDocsGradleKotlinBuildCustomizer restDocsGradleKotlinBuildCustomizer() {
		return new SpringRestDocsGradleKotlinBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	SpringRestDocsMavenBuildCustomizer restDocsMavenBuildCustomizer() {
		return new SpringRestDocsMavenBuildCustomizer();
	}

}
