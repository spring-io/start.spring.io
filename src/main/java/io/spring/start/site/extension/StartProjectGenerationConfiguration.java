/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.start.site.extension.springcloud.SpringCloudProjectGenerationConfiguration;
import io.spring.start.site.extension.springrestdocs.SpringRestDocsProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Configuration for {@link BuildCustomizer}s.
 *
 * @author Madhura Bhave
 */
@ProjectGenerationConfiguration
@Import({ SpringCloudProjectGenerationConfiguration.class,
		SpringRestDocsProjectGenerationConfiguration.class })
public class StartProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("webflux")
	public ReactorTestBuildCustomizer reactorTestBuildCustomizer(
			ResolvedProjectDescription description) {
		return new ReactorTestBuildCustomizer(description);
	}

	@Bean
	@ConditionalOnRequestedDependency("security")
	public SpringSecurityTestBuildCustomizer securityTestBuildCustomizer() {
		return new SpringSecurityTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("batch")
	public SpringBatchTestBuildCustomizer batchTestBuildCustomizer() {
		return new SpringBatchTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("lombok")
	public LombokGradleBuildCustomizer lombokGradleBuildCustomizer(
			InitializrMetadata metadata) {
		return new LombokGradleBuildCustomizer(metadata);
	}

	@Bean
	public JacksonKotlinBuildCustomizer jacksonKotlinBuildCustomizer(
			InitializrMetadata metadata, ResolvedProjectDescription description) {
		return new JacksonKotlinBuildCustomizer(metadata, description);
	}

	@Bean
	public SpringKafkaBuildCustomizer springKafkaBuildCustomizer(
			ResolvedProjectDescription description) {
		return new SpringKafkaBuildCustomizer(description);
	}

	@Bean
	@ConditionalOnRequestedDependency("session")
	public SpringSessionBuildCustomizer springSessionBuildCustomizer(
			ResolvedProjectDescription description) {
		return new SpringSessionBuildCustomizer(description);
	}

	@Bean
	@ConditionalOnRequestedDependency("flyway")
	public FlywayProjectContributor flywayProjectContributor() {
		return new FlywayProjectContributor();
	}

	@Bean
	@ConditionalOnRequestedDependency("restdocs")
	public SpringRestDocsBuildCustomizer springRestDocsBuildCustomizer() {
		return new SpringRestDocsBuildCustomizer();
	}

}
