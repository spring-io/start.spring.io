/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.build.gradle;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.gradle.DependencyManagementPluginVersionResolver;
import io.spring.initializr.generator.spring.build.gradle.InitializrDependencyManagementPluginVersionResolver;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.MavenVersionResolver;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that depend on
 * Gradle.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
@ConditionalOnBuildSystem(GradleBuildSystem.ID)
class GradleProjectGenerationConfiguration {

	@Bean
	GradleBuildSystemHelpDocumentCustomizer gradleBuildSystemHelpDocumentCustomizer(ProjectDescription description) {
		return new GradleBuildSystemHelpDocumentCustomizer(description);
	}

	@Bean
	DependencyManagementPluginVersionResolver dependencyManagementPluginVersionResolver(
			MavenVersionResolver versionResolver, InitializrMetadata metadata) {
		return new ManagedDependenciesDependencyManagementPluginVersionResolver(versionResolver,
				(description) -> new InitializrDependencyManagementPluginVersionResolver(metadata)
					.resolveDependencyManagementPluginVersion(description));
	}

}
