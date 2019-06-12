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

package io.spring.start.site.extension;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.spring.build.gradle.ConditionalOnGradleVersion;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.start.site.extension.springboot.SpringBootProjectGenerationConfiguration;
import io.spring.start.site.extension.springcloud.SpringCloudProjectGenerationConfiguration;
import io.spring.start.site.extension.springrestdocs.SpringRestDocsProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * {@link ProjectGenerationConfiguration} for components that tune the build according to
 * the settings of this instance.
 *
 * @author Madhura Bhave
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
@Import({ SpringBootProjectGenerationConfiguration.class, SpringCloudProjectGenerationConfiguration.class,
		SpringRestDocsProjectGenerationConfiguration.class })
public class StartProjectGenerationConfiguration {

	private final InitializrMetadata metadata;

	private final ResolvedProjectDescription description;

	public StartProjectGenerationConfiguration(InitializrMetadata metadata, ResolvedProjectDescription description) {
		this.metadata = metadata;
		this.description = description;
	}

	@Bean
	@ConditionalOnRequestedDependency("webflux")
	public ReactorTestBuildCustomizer reactorTestBuildCustomizer() {
		return new ReactorTestBuildCustomizer(this.description);
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
	@ConditionalOnGradleVersion({ "4", "5" })
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	@ConditionalOnRequestedDependency("lombok")
	public LombokGradleBuildCustomizer lombokGradleBuildCustomizer() {
		return new LombokGradleBuildCustomizer(this.metadata);
	}

	@Bean
	public JacksonKotlinBuildCustomizer jacksonKotlinBuildCustomizer() {
		return new JacksonKotlinBuildCustomizer(this.metadata, this.description);
	}

	@Bean
	public SpringKafkaBuildCustomizer springKafkaBuildCustomizer() {
		return new SpringKafkaBuildCustomizer(this.description);
	}

	@Bean
	@ConditionalOnRequestedDependency("session")
	public SpringSessionBuildCustomizer springSessionBuildCustomizer() {
		return new SpringSessionBuildCustomizer(this.description);
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

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	public GradleBuildSystemHelpDocumentCustomizer gradleBuildSystemHelpDocumentCustomizer() {
		return new GradleBuildSystemHelpDocumentCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	public MavenBuildSystemHelpDocumentCustomizer mavenBuildSystemHelpDocumentCustomizer() {
		return new MavenBuildSystemHelpDocumentCustomizer();
	}

}
