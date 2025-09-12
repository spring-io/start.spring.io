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

package io.spring.start.site.extension.dependency.springazure;

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
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring Azure Docker Compose.
 *
 * @author Eddú Meléndez
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("azure-storage")
class SpringAzureDockerComposeProjectGenerationConfiguration {

	private static final String DEPENDENCY_ID = "spring-azure-docker-compose";

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	BuildCustomizer<Build> springAzureDockerComposeBuildCustomizer() {
		return (build) -> build.dependencies()
			.add(DEPENDENCY_ID, Dependency.withCoordinates("com.azure.spring", "spring-cloud-azure-docker-compose")
				.scope(DependencyScope.RUNTIME));
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	OptionalDependencyMavenBuildCustomizer springAzureDockerComposeMavenBuildCustomizer() {
		return new OptionalDependencyMavenBuildCustomizer(DEPENDENCY_ID);
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	DevelopmentOnlyDependencyGradleBuildCustomizer springAzureDockerComposeGradleBuildCustomizer() {
		return new DevelopmentOnlyDependencyGradleBuildCustomizer(DEPENDENCY_ID);
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer azureStorageComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("azurite",
				(service) -> composeFile.services().add("azurite", service));
	}

}
