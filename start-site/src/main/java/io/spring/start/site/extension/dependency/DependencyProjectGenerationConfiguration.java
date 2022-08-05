/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.start.site.extension.dependency;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.gradle.ConditionalOnGradleVersion;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.start.site.extension.dependency.graphql.SpringGraphQlBuildCustomizer;
import io.spring.start.site.extension.dependency.liquibase.LiquibaseProjectContributor;
import io.spring.start.site.extension.dependency.lombok.LombokGradleBuildCustomizer;
import io.spring.start.site.extension.dependency.okta.OktaHelpDocumentCustomizer;
import io.spring.start.site.extension.dependency.reactor.ReactorTestBuildCustomizer;
import io.spring.start.site.extension.dependency.springbatch.SpringBatchTestBuildCustomizer;
import io.spring.start.site.extension.dependency.springkafka.SpringKafkaBuildCustomizer;
import io.spring.start.site.extension.dependency.springsecurity.SpringSecurityRSocketBuildCustomizer;
import io.spring.start.site.extension.dependency.springsecurity.SpringSecurityTestBuildCustomizer;
import io.spring.start.site.extension.dependency.springsession.SpringSessionBuildCustomizer;
import io.spring.start.site.extension.dependency.thymeleaf.ThymeleafBuildCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for customizations relevant to selected
 * dependencies.
 *
 * @author Madhura Bhave
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
public class DependencyProjectGenerationConfiguration {

	private final InitializrMetadata metadata;

	public DependencyProjectGenerationConfiguration(InitializrMetadata metadata) {
		this.metadata = metadata;
	}

	@Bean
	public ReactorTestBuildCustomizer reactorTestBuildCustomizer() {
		return new ReactorTestBuildCustomizer(this.metadata);
	}

	@Bean
	@ConditionalOnRequestedDependency("security")
	public SpringSecurityTestBuildCustomizer securityTestBuildCustomizer() {
		return new SpringSecurityTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("security")
	public SpringSecurityRSocketBuildCustomizer securityRSocketBuildCustomizer() {
		return new SpringSecurityRSocketBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("graphql")
	public SpringGraphQlBuildCustomizer graphQlBuildCustomizer() {
		return new SpringGraphQlBuildCustomizer(this.metadata);
	}

	@Bean
	@ConditionalOnRequestedDependency("batch")
	public SpringBatchTestBuildCustomizer batchTestBuildCustomizer() {
		return new SpringBatchTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnGradleVersion({ "6", "7" })
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	@ConditionalOnRequestedDependency("lombok")
	public LombokGradleBuildCustomizer lombokGradleBuildCustomizer() {
		return new LombokGradleBuildCustomizer(this.metadata);
	}

	@Bean
	public SpringKafkaBuildCustomizer springKafkaBuildCustomizer() {
		return new SpringKafkaBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("session")
	public SpringSessionBuildCustomizer springSessionBuildCustomizer() {
		return new SpringSessionBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("thymeleaf")
	public ThymeleafBuildCustomizer thymeleafBuildCustomizer(ProjectDescription description) {
		return new ThymeleafBuildCustomizer(description.getPlatformVersion());
	}

	@Bean
	@ConditionalOnRequestedDependency("liquibase")
	public LiquibaseProjectContributor liquibaseProjectContributor() {
		return new LiquibaseProjectContributor();
	}

	@Bean
	@ConditionalOnRequestedDependency("okta")
	public OktaHelpDocumentCustomizer oktaHelpDocumentCustomizer(MustacheTemplateRenderer templateRenderer) {
		return new OktaHelpDocumentCustomizer(templateRenderer);
	}

}
