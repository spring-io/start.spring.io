/*
 * Copyright 2012 - present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import io.spring.initializr.generator.version.VersionProperty;
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

	private static VersionProperty getSpringAiVersion(InitializrMetadata metadata) {
		return metadata.getConfiguration().getEnv().getBoms().get("spring-ai").getVersionProperty();
	}

}
