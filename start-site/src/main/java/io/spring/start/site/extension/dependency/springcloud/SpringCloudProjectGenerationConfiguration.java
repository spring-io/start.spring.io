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

package io.spring.start.site.extension.dependency.springcloud;

import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.MavenVersionResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that depend on Spring
 * Cloud.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
public class SpringCloudProjectGenerationConfiguration {

	private final InitializrMetadata metadata;

	private final ProjectDescription description;

	public SpringCloudProjectGenerationConfiguration(InitializrMetadata metadata, ProjectDescription description) {
		this.metadata = metadata;
		this.description = description;
	}

	@Bean
	public SpringCloudFunctionBuildCustomizer springCloudFunctionBuildCustomizer() {
		return new SpringCloudFunctionBuildCustomizer(this.metadata, this.description);
	}

	@Bean
	public SpringCloudStreamBuildCustomizer springCloudStreamBuildCustomizer() {
		return new SpringCloudStreamBuildCustomizer(this.description);
	}

	@Bean
	SpringCloudProjectVersionResolver springCloudProjectVersionResolver(MavenVersionResolver versionResolver) {
		return new SpringCloudProjectVersionResolver(this.metadata, versionResolver);
	}

	@Bean
	public SpringCloudFunctionHelpDocumentCustomizer springCloudFunctionHelpDocumentCustomizer(Build build,
			MustacheTemplateRenderer templateRenderer, SpringCloudProjectVersionResolver versionResolver) {
		return new SpringCloudFunctionHelpDocumentCustomizer(build, this.description, templateRenderer,
				versionResolver);
	}

	@Bean
	public SpringCloudCircuitBreakerBuildCustomizer springCloudCircuitBreakerBuildCustomizer() {
		return new SpringCloudCircuitBreakerBuildCustomizer(this.metadata, this.description);
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnRequestedDependency("cloud-contract-verifier")
	static class SpringCloudContractConfiguration {

		@Bean
		ProjectContributor springCloudContractDirectoryProjectContributor(Build build) {
			String contractDirectory = (build instanceof MavenBuild) ? "src/test/resources/contracts"
					: "src/contractTest/resources/contracts";
			return (projectRoot) -> {
				Path migrationDirectory = projectRoot.resolve(contractDirectory);
				Files.createDirectories(migrationDirectory);
			};
		}

		@Bean
		@ConditionalOnBuildSystem(MavenBuildSystem.ID)
		SpringCloudContractMavenBuildCustomizer springCloudContractMavenBuildCustomizer(ProjectDescription description,
				SpringCloudProjectVersionResolver versionResolver) {
			return new SpringCloudContractMavenBuildCustomizer(description, versionResolver);
		}

		@Bean
		@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
		SpringCloudContractGroovyDslGradleBuildCustomizer springCloudContractGroovyDslGradleBuildCustomizer(
				ProjectDescription description, SpringCloudProjectVersionResolver versionResolver) {
			return new SpringCloudContractGroovyDslGradleBuildCustomizer(description, versionResolver);
		}

		@Bean
		@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
		SpringCloudContractKotlinDslGradleBuildCustomizer springCloudContractKotlinDslGradleBuildCustomizer(
				ProjectDescription description, SpringCloudProjectVersionResolver versionResolver) {
			return new SpringCloudContractKotlinDslGradleBuildCustomizer(description, versionResolver);
		}

	}

}
