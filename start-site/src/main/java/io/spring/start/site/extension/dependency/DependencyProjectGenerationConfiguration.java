/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.start.site.extension.dependency.ldap.LdapUnboundIdBuildCustomizer;
import io.spring.start.site.extension.dependency.liquibase.LiquibaseProjectContributor;
import io.spring.start.site.extension.dependency.lombok.LombokGradleBuildCustomizer;
import io.spring.start.site.extension.dependency.lombok.LombokMavenBuildCustomizer;
import io.spring.start.site.extension.dependency.mybatis.MyBatisTestBuildCustomizer;
import io.spring.start.site.extension.dependency.okta.OktaHelpDocumentCustomizer;
import io.spring.start.site.extension.dependency.reactor.ReactorTestBuildCustomizer;
import io.spring.start.site.extension.dependency.springsecurity.SpringSecurityLdapBuildCustomizer;
import io.spring.start.site.extension.dependency.springsecurity.SpringSecurityRSocketBuildCustomizer;
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
	AddTestStartersBuildCustomizer addTestStartersBuildCustomizer() {
		return new AddTestStartersBuildCustomizer();
	}

	@Bean
	public ReactorTestBuildCustomizer reactorTestBuildCustomizer(ProjectDescription description) {
		return new ReactorTestBuildCustomizer(this.metadata, description);
	}

	@Bean
	@ConditionalOnRequestedDependency("security")
	public SpringSecurityRSocketBuildCustomizer securityRSocketBuildCustomizer() {
		return new SpringSecurityRSocketBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("security")
	public SpringSecurityLdapBuildCustomizer securityLdapBuildCustomizer() {
		return new SpringSecurityLdapBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("spring-security-webauthn")
	SpringSecurityWebAuthnBuildCustomizer springSecurityWebAuthnBuildCustomizer() {
		return new SpringSecurityWebAuthnBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	@ConditionalOnRequestedDependency("lombok")
	public LombokGradleBuildCustomizer lombokGradleBuildCustomizer() {
		return new LombokGradleBuildCustomizer(this.metadata);
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	@ConditionalOnRequestedDependency("lombok")
	LombokMavenBuildCustomizer lombokMavenBuildCustomizer() {
		return new LombokMavenBuildCustomizer(this.metadata);
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
	LdapUnboundIdBuildCustomizer ldapUnboundIdBuildCustomizer() {
		return new LdapUnboundIdBuildCustomizer();
	}

}
