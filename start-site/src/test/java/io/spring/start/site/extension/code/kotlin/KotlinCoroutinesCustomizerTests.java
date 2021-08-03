/*
 * Copyright 2012-2021 the original author or authors.
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

package io.spring.start.site.extension.code.kotlin;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link KotlinCoroutinesCustomizer}.
 *
 * @author Stephane Nicoll
 */
class KotlinCoroutinesCustomizerTests extends AbstractExtensionTests {

	@Test
	void kotlinCoroutinesIsAdded() {
		ProjectRequest request = createProjectRequest("webflux");
		request.setBootVersion("2.5.0");
		request.setLanguage("kotlin");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild().hasDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor");
		assertThat(project).textFile("HELP.md").contains(
				"* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.3.7/spring-framework-reference/languages.html#coroutines)");
	}

	@Test
	void kotlinCoroutinesIsNotAddedWithNonKotlinApp() {
		ProjectRequest request = createProjectRequest("webflux");
		request.setBootVersion("2.5.0");
		request.setLanguage("java");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor");
	}

	@Test
	void kotlinCoroutinesIsNotAddedWithoutReactiveFacet() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.5.0");
		request.setLanguage("kotlin");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor");
	}

}
