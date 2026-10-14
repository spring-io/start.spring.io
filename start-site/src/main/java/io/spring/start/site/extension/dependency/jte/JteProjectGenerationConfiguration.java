/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.jte;

import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.generator.spring.properties.ApplicationPropertiesCustomizer;
import io.spring.initializr.generator.spring.scm.git.GitIgnoreCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for jte.
 *
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("jte")
class JteProjectGenerationConfiguration {

	@Bean
	ProjectContributor jteTemplateDirectoryProjectContributor() {
		return (projectRoot) -> {
			Path templatesDirectory = projectRoot.resolve("src/main/jte");
			Files.createDirectories(templatesDirectory);
		};
	}

	@Bean
	ApplicationPropertiesCustomizer jteApplicationPropertiesCustomizer() {
		return (properties) -> properties.add("gg.jte.development-mode", true);
	}

	@Bean
	GitIgnoreCustomizer jteGitIgnoreCustomizer() {
		return (gitignore) -> gitignore.addSectionIfAbsent("jte").add("/jte-classes/");
	}

	@Bean
	JteHelpDocumentCustomizer jteHelpDocumentCustomizer() {
		return new JteHelpDocumentCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	JteGradleBuildCustomizer jteGradleBuildCustomizer() {
		return new JteGradleBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	JteMavenBuildCustomizer jteMavenBuildCustomizer() {
		return new JteMavenBuildCustomizer();
	}

}
