/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that use the Netflix
 * DGS codegen.
 *
 * @author Brian Clozel
 * @see <a href="https://netflix.github.io/dgs/generating-code-from-schema/">Netflix DGS
 * codegen</a>
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("dgs-codegen")
@ConditionalOnPlatformVersion("3.0.0-M1")
class DgsCodegenProjectGenerationConfiguration {

	private final String dgsCodegenPluginVersion;

	DgsCodegenProjectGenerationConfiguration(InitializrMetadata metadata, ProjectDescription description) {
		this.dgsCodegenPluginVersion = DgsCodegenVersionResolver.resolve(metadata, description.getPlatformVersion(),
				description.getBuildSystem());
	}

	@Bean
	DgsCodegenBuildCustomizer dgsCodegenBuildCustomizer() {
		return new DgsCodegenBuildCustomizer();
	}

	@Bean
	DgsCodegenProjectContributor dgsCodegenProjectContributor() {
		return new DgsCodegenProjectContributor();
	}

	@Bean
	DgsCodegenHelpDocumentCustomizer dgsCodegenHelpDocumentCustomizer(ProjectDescription description) {
		return new DgsCodegenHelpDocumentCustomizer(description);
	}

	@Bean
	@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
	DgsCodegenGroovyDslGradleBuildCustomizer dgsCodegenGroovyDslGradleBuildCustomizer(ProjectDescription description) {
		return new DgsCodegenGroovyDslGradleBuildCustomizer(this.dgsCodegenPluginVersion, description.getPackageName());
	}

	@Bean
	@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
	DgsCodegenKotlinDslGradleBuildCustomizer dgsCodegenKotlinDslGradleBuildCustomizer(ProjectDescription description) {
		return new DgsCodegenKotlinDslGradleBuildCustomizer(this.dgsCodegenPluginVersion, description.getPackageName());
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	DgsCodegenMavenBuildCustomizer dgsCodegenMavenBuildCustomizer(ProjectDescription description) {
		return new DgsCodegenMavenBuildCustomizer(this.dgsCodegenPluginVersion, description.getPackageName());
	}

}
