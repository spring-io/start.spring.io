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

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.build.gradle.ConditionalOnGradleVersion;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;
import io.spring.start.site.extension.springboot.SpringBootProjectGenerationConfiguration;
import io.spring.start.site.extension.springcloud.SpringCloudProjectGenerationConfiguration;
import io.spring.start.site.extension.springrestdocs.SpringRestDocsProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link ProjectGenerationConfiguration} for components that tune the build according to
 * the settings of this instance.
 *
 * @author Madhura Bhave
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@Import({ SpringBootProjectGenerationConfiguration.class, SpringCloudProjectGenerationConfiguration.class,
		SpringRestDocsProjectGenerationConfiguration.class })
public class StartProjectGenerationConfiguration {

	private final InitializrMetadata metadata;

	private final ProjectDescription description;

	public StartProjectGenerationConfiguration(InitializrMetadata metadata, ProjectDescription description) {
		this.metadata = metadata;
		this.description = description;
	}

	@Bean
	public ReactorTestBuildCustomizer reactorTestBuildCustomizer() {
		return new ReactorTestBuildCustomizer(this.metadata, this.description);
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
	@ConditionalOnRequestedDependency("liquibase")
	public LiquibaseProjectContributor liquibaseProjectContributor() {
		return new LiquibaseProjectContributor();
	}

	@Bean
	@ConditionalOnRequestedDependency("restdocs")
	public SpringRestDocsBuildCustomizer springRestDocsBuildCustomizer() {
		return new SpringRestDocsBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("cloud-contract-verifier")
	public SpringCloudContractDirectoryProjectContributor springCloudContractContributor() {
		return new SpringCloudContractDirectoryProjectContributor();
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	public GradleBuildSystemHelpDocumentCustomizer gradleBuildSystemHelpDocumentCustomizer() {
		return new GradleBuildSystemHelpDocumentCustomizer(this.description);
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	public MavenBuildSystemHelpDocumentCustomizer mavenBuildSystemHelpDocumentCustomizer() {
		return new MavenBuildSystemHelpDocumentCustomizer(this.description);
	}

	@Configuration
	@ConditionalOnLanguage(KotlinLanguage.ID)
	@ConditionalOnPlatformVersion("2.2.0.M5")
	static class KotlinCoroutinesCustomizerConfiguration {

		private final KotlinCoroutinesCustomizer customizer;

		KotlinCoroutinesCustomizerConfiguration(InitializrMetadata metadata, ProjectDescription description,
				DependencyManagementVersionResolver versionResolver) {
			this.customizer = new KotlinCoroutinesCustomizer(metadata, description, versionResolver);
		}

		@Bean
		BuildCustomizer<Build> kotlinCoroutinesBuildCustomizer() {
			return this.customizer::customize;
		}

		@Bean
		HelpDocumentCustomizer kotlinCoroutinesHelpDocumentCustomizer(Build build) {
			return (helpDocument) -> this.customizer.customize(helpDocument, build);
		}

	}

}
