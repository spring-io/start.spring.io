/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.hilla;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HillaProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 */
class HillaProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void mavenBuildWithHillaAddProductionProfile() {
		ProjectRequest request = createProjectRequest("hilla", "data-jpa");
		request.setBootVersion("3.1.0");
		assertThat(mavenPom(request)).hasProfile("production")
			.contains(new ClassPathResource("dependency/hilla/hilla-maven-profile.xml"));
	}

	@Test
	void mavenBuildWithoutHillaDoesNotAddProductionProfile() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("3.1.0");
		assertThat(mavenPom(request)).doesNotContain("hilla");
	}

	@Test
	void gradleBuildWithHillaConfigurePlugin() {
		ProjectRequest request = createProjectRequest("hilla", "data-jpa");
		request.setBootVersion("3.1.0");
		assertThat(gradleBuild(request)).hasPlugin("dev.hilla");
	}

	@Test
	void gradleBuildWithHillaConfigureTask() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("3.1.0");
		assertThat(gradleBuild(request)).doesNotContain("hilla");
	}

	@Test
	void projectWithHillaCustomizesGitIgnore() {
		ProjectRequest request = createProjectRequest("hilla", "data-jpa");
		request.setBootVersion("3.1.0");
		assertThat(generateProject(request)).textFile(".gitignore").contains("node_modules/", "frontend/generated/");
	}

	@Test
	void projectWithoutHillaDoesNotCustomizeGitIgnore() {
		assertThat(generateProject(createProjectRequest("data-jpa"))).textFile(".gitignore")
			.doesNotContain("frontend/generated/");
	}

}
