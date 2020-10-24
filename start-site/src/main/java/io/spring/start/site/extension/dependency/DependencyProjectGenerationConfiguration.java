/*
 * Copyright 2012-2020 the original author or authors.
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
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.gradle.ConditionalOnGradleVersion;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.start.site.extension.dependency.flyway.FlywayProjectContributor;
import io.spring.start.site.extension.dependency.liquibase.LiquibaseProjectContributor;
import io.spring.start.site.extension.dependency.lombok.LombokGradleBuildCustomizer;
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

	private final ProjectDescription description;

	public DependencyProjectGenerationConfiguration(InitializrMetadata metadata, ProjectDescription description) {
		this.metadata = metadata;
		this.description = description;
	}

	@Bean
	@ConditionalOnPlatformVersion("2.0.0.M2")
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
	@ConditionalOnRequestedDependency("batch")
	public SpringBatchTestBuildCustomizer batchTestBuildCustomizer() {
		return new SpringBatchTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnGradleVersion({ "4", "5", "6" })
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
		return new SpringSessionBuildCustomizer(this.description);
	}

	@Bean
	@ConditionalOnRequestedDependency("thymeleaf")
	public ThymeleafBuildCustomizer thymeleafBuildCustomizer() {
		return new ThymeleafBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("flyway")
	public FlywayProjectContributor flywayProjectContributor() {
		return new FlywayProjectContributor();
	}

	@Bean
	@ConditionalOnRequestedDependency("liquibase")
	public LiquibaseProjectContributor liquibaseProjectContributor() {
		return new LiquibaseProjectContributor();
	}

}
