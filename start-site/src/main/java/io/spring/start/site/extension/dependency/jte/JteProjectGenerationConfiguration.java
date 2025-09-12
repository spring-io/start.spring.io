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
 * {@link ProjectGenerationConfiguration} for JTE.
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
		return (gitignore) -> gitignore.addSectionIfAbsent("JTE").add("/jte-classes/");
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
