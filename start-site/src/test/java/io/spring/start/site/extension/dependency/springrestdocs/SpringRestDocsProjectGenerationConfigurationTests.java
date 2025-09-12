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
