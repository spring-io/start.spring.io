/*
 * Copyright 2012-2021 the original author or authors.
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

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionDiff;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;

import org.springframework.context.annotation.Bean;

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
		return new SpringCloudStreamBuildCustomizer();
	}

	@Bean
	SpringCloudProjectVersionResolver springCloudProjectVersionResolver(
			DependencyManagementVersionResolver versionResolver) {
		return new SpringCloudProjectVersionResolver(this.metadata, versionResolver);
	}

	@Bean
	@ConditionalOnRequestedDependency("cloud-contract-verifier")
	public SpringCloudContractDirectoryProjectContributor springCloudContractContributor() {
		return new SpringCloudContractDirectoryProjectContributor();
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	@ConditionalOnRequestedDependency("cloud-contract-verifier")
	SpringCloudContractMavenBuildCustomizer springCloudContractMavenBuildCustomizer(
			SpringCloudProjectVersionResolver versionResolver) {
		return new SpringCloudContractMavenBuildCustomizer(this.description, versionResolver);
	}

	@Bean
	@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
	@ConditionalOnRequestedDependency("cloud-contract-verifier")
	SpringCloudContractGradleBuildCustomizer springCloudContractGradleBuildCustomizer(
			SpringCloudProjectVersionResolver versionResolver) {
		return new SpringCloudContractGradleBuildCustomizer(this.description, versionResolver);
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

	@Bean
	@ConditionalOnRequestedDependency("cloud-gateway")
	public SpringCloudGatewayHelpDocumentCustomizer springCloudGatewayHelpDocumentCustomizer(
			ProjectDescriptionDiff diff) {
		return new SpringCloudGatewayHelpDocumentCustomizer(diff);
	}

}
