/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.build.gradle.DevelopmentOnlyDependencyGradleBuildCustomizer;
import io.spring.initializr.generator.spring.build.maven.OptionalDependencyMavenBuildCustomizer;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring AI Docker Compose.
 *
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("docker-compose")
@ConditionalOnRequestedSpringAiDependency
public class SpringAiDockerComposeProjectGenerationConfiguration {

	/**
	 * Dependency id of
	 * {@code org.springframework.ai:spring-ai-spring-boot-docker-compose}.
	 */
	private static final String DEPENDENCY_ID = "spring-ai-docker-compose";

	@Bean
	BuildCustomizer<Build> springAiDockerComposeBuildCustomizer(InitializrMetadata metadata) {
		return (build) -> build.dependencies()
			.add(DEPENDENCY_ID,
					Dependency.withCoordinates("org.springframework.ai", "spring-ai-spring-boot-docker-compose")
						.scope(DependencyScope.RUNTIME));
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	DevelopmentOnlyDependencyGradleBuildCustomizer springAiDockerComposeGradleBuildCustomizer() {
		return new DevelopmentOnlyDependencyGradleBuildCustomizer(
				SpringAiDockerComposeProjectGenerationConfiguration.DEPENDENCY_ID);
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	OptionalDependencyMavenBuildCustomizer springAiDockerComposeMavenBuildCustomizer() {
		return new OptionalDependencyMavenBuildCustomizer(
				SpringAiDockerComposeProjectGenerationConfiguration.DEPENDENCY_ID);
	}

}
