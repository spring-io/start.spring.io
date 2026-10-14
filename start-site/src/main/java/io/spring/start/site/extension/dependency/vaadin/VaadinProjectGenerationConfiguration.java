/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.vaadin;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.properties.ApplicationPropertiesCustomizer;
import io.spring.initializr.generator.spring.scm.git.GitIgnoreCustomizer;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Vaadin.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("vaadin")
class VaadinProjectGenerationConfiguration {

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	VaadinMavenBuildCustomizer vaadinMavenBuildCustomizer() {
		return new VaadinMavenBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	VaadinGradleBuildCustomizer vaadinGradleBuildCustomizer(InitializrMetadata metadata,
			ProjectDescription description) {
		return new VaadinGradleBuildCustomizer(metadata, description.getPlatformVersion());
	}

	@Bean
	GitIgnoreCustomizer vaadinGitIgnoreCustomizer() {
		return (gitignore) -> gitignore.getGeneral().add("node_modules");
	}

	@Bean
	ApplicationPropertiesCustomizer launchBrowserApplicationPropertiesCustomizer() {
		return (properties) -> properties.add("vaadin.launch-browser", true);
	}

}
