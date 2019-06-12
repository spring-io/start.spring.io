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

package io.spring.start.site.extension.springrestdocs;

import java.util.Map;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.test.project.ProjectAssetTester;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SpringRestDocsProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 */
class SpringRestDocsProjectGenerationConfigurationTests {

	private final ProjectAssetTester projectTester = new ProjectAssetTester()
			.withConfiguration(SpringRestDocsProjectGenerationConfiguration.class);

	@Test
	void springRestDocsCustomizerMaven() {
		ProjectDescription description = new ProjectDescription();
		description.setBuildSystem(new MavenBuildSystem());
		description.addDependency("restdocs", mock(Dependency.class));
		Map<String, BuildCustomizer> buildCustomizers = this.projectTester.generate(description,
				(context) -> context.getBeansOfType(BuildCustomizer.class));
		assertThat(buildCustomizers).containsOnlyKeys("restDocsMavenBuildCustomizer");
	}

	@Test
	void springRestDocsCustomizerGradle() {
		ProjectDescription description = new ProjectDescription();
		description.setBuildSystem(new GradleBuildSystem());
		description.addDependency("restdocs", mock(Dependency.class));
		Map<String, BuildCustomizer> buildCustomizers = this.projectTester.generate(description,
				(context) -> context.getBeansOfType(BuildCustomizer.class));
		assertThat(buildCustomizers).containsOnlyKeys("restDocsGradleBuildCustomizer");
	}

	@Test
	void springRestDocsNotAppliedIfRestDocsNotSelected() {
		ProjectDescription description = new ProjectDescription();
		description.setBuildSystem(new GradleBuildSystem());
		description.addDependency("web", mock(Dependency.class));
		Map<String, BuildCustomizer> buildCustomizers = this.projectTester.generate(description,
				(context) -> context.getBeansOfType(BuildCustomizer.class));
		assertThat(buildCustomizers).isEmpty();
	}

}
