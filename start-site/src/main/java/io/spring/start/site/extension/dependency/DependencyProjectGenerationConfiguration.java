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

package io.spring.start.site.extension.dependency;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.start.site.extension.dependency.ldap.LdapUnboundIdBuildCustomizer;
import io.spring.start.site.extension.dependency.liquibase.LiquibaseProjectContributor;
import io.spring.start.site.extension.dependency.lombok.LombokGradleBuildCustomizer;
import io.spring.start.site.extension.dependency.mybatis.MyBatisTestBuildCustomizer;
import io.spring.start.site.extension.dependency.okta.OktaHelpDocumentCustomizer;
import io.spring.start.site.extension.dependency.reactor.ReactorTestBuildCustomizer;
import io.spring.start.site.extension.dependency.springbatch.SpringBatchTestBuildCustomizer;
import io.spring.start.site.extension.dependency.springsecurity.SpringSecurityRSocketBuildCustomizer;
import io.spring.start.site.extension.dependency.springsecurity.SpringSecurityTestBuildCustomizer;
import io.spring.start.site.extension.dependency.springsecurity.SpringSecurityWebAuthnBuildCustomizer;
import io.spring.start.site.extension.dependency.springshell.SpringShellTestBuildCustomizer;
import io.spring.start.site.extension.dependency.thymeleaf.ThymeleafBuildCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for customizations relevant to selected
 * dependencies.
 *
 * @author Madhura Bhave
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 * @author Kazuki Shimizu
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
public class DependencyProjectGenerationConfiguration {

	private final InitializrMetadata metadata;

	public DependencyProjectGenerationConfiguration(InitializrMetadata metadata) {
		this.metadata = metadata;
	}

	@Bean
	@ConditionalOnPlatformVersion("4.0.0-RC1")
	AddTestStartersBuildCustomizer addTestStartersBuildCustomizer() {
		return new AddTestStartersBuildCustomizer();
	}

	@Bean
	public ReactorTestBuildCustomizer reactorTestBuildCustomizer(ProjectDescription description) {
		return new ReactorTestBuildCustomizer(this.metadata, description);
	}

	@Bean
	@ConditionalOnRequestedDependency("security")
	@ConditionalOnPlatformVersion("[3.5.0,4.0.0-RC1)")
	public SpringSecurityTestBuildCustomizer securityTestBuildCustomizer() {
		return new SpringSecurityTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("oauth2-client")
	@ConditionalOnPlatformVersion("[3.5.0,4.0.0-RC1)")
	SpringSecurityTestBuildCustomizer oauth2ClientTestBuildCustomizer() {
		return new SpringSecurityTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("security")
	public SpringSecurityRSocketBuildCustomizer securityRSocketBuildCustomizer() {
		return new SpringSecurityRSocketBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("spring-security-webauthn")
	SpringSecurityWebAuthnBuildCustomizer springSecurityWebAuthnBuildCustomizer() {
		return new SpringSecurityWebAuthnBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("batch")
	@ConditionalOnPlatformVersion("[3.5.0,4.0.0-RC1]")
	public SpringBatchTestBuildCustomizer batchTestBuildCustomizer() {
		return new SpringBatchTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	@ConditionalOnRequestedDependency("lombok")
	public LombokGradleBuildCustomizer lombokGradleBuildCustomizer() {
		return new LombokGradleBuildCustomizer(this.metadata);
	}

	@Bean
	@ConditionalOnRequestedDependency("thymeleaf")
	public ThymeleafBuildCustomizer thymeleafBuildCustomizer() {
		return new ThymeleafBuildCustomizer();
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

	@Bean
	@ConditionalOnRequestedDependency("mybatis")
	public MyBatisTestBuildCustomizer mybatisTestBuildCustomizer() {
		return new MyBatisTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("spring-shell")
	public SpringShellTestBuildCustomizer springShellTestBuildCustomizer() {
		return new SpringShellTestBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("unboundid-ldap")
	@ConditionalOnPlatformVersion("4.0.0-M1")
	LdapUnboundIdBuildCustomizer ldapUnboundIdBuildCustomizer() {
		return new LdapUnboundIdBuildCustomizer();
	}

}
