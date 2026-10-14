/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.test.project.ProjectAssetTester;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SpringRestDocsProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class SpringRestDocsProjectGenerationConfigurationTests {

	private final ProjectAssetTester projectTester = new ProjectAssetTester()
		.withConfiguration(SpringRestDocsProjectGenerationConfiguration.class);

	@Test
	void springRestDocsCustomizerMaven() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setBuildSystem(new MavenBuildSystem());
		description.addDependency("restdocs", mock(Dependency.class));
		this.projectTester.configure(description,
				(context) -> assertThat(context).getBeans(BuildCustomizer.class)
					.containsKeys("restDocsMavenBuildCustomizer")
					.doesNotContainKeys("restDocsGradleBuildCustomizer"));
	}

	@Test
	void springRestDocsCustomizerGradleGroovy() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setBuildSystem(new GradleBuildSystem(GradleBuildSystem.DIALECT_GROOVY));
		description.addDependency("restdocs", mock(Dependency.class));
		this.projectTester.configure(description,
				(context) -> assertThat(context).getBeans(BuildCustomizer.class)
					.containsKeys("restDocsGradleGroovyBuildCustomizer")
					.doesNotContainKeys("restDocsGradleKotlinBuildCustomizer")
					.doesNotContainKeys("restDocsMavenBuildCustomizer"));
	}

	@Test
	void springRestDocsCustomizerGradleKotlin() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setBuildSystem(new GradleBuildSystem(GradleBuildSystem.DIALECT_KOTLIN));
		description.addDependency("restdocs", mock(Dependency.class));
		this.projectTester.configure(description,
				(context) -> assertThat(context).getBeans(BuildCustomizer.class)
					.containsKeys("restDocsGradleKotlinBuildCustomizer")
					.doesNotContainKeys("restDocsGradleGroovyBuildCustomizer")
					.doesNotContainKeys("restDocsMavenBuildCustomizer"));
	}

	@Test
	void springRestDocsNotAppliedIfRestDocsNotSelected() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setBuildSystem(new GradleBuildSystem());
		description.addDependency("web", mock(Dependency.class));
		this.projectTester.configure(description,
				(context) -> assertThat(context).getBeans(BuildCustomizer.class).isEmpty());
	}

}
