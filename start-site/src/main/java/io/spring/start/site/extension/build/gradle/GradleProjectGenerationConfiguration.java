/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.build.gradle;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.gradle.DependencyManagementPluginVersionResolver;
import io.spring.initializr.generator.spring.build.gradle.InitializrDependencyManagementPluginVersionResolver;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that depend on
 * Gradle.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
@ConditionalOnBuildSystem(GradleBuildSystem.ID)
class GradleProjectGenerationConfiguration {

	@Bean
	GradleBuildSystemHelpDocumentCustomizer gradleBuildSystemHelpDocumentCustomizer(ProjectDescription description) {
		return new GradleBuildSystemHelpDocumentCustomizer(description);
	}

	@Bean
	DependencyManagementPluginVersionResolver dependencyManagementPluginVersionResolver(
			DependencyManagementVersionResolver versionResolver, InitializrMetadata metadata) {
		return new ManagedDependenciesDependencyManagementPluginVersionResolver(versionResolver,
				(description) -> new InitializrDependencyManagementPluginVersionResolver(metadata)
						.resolveDependencyManagementPluginVersion(description));
	}

}
