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

package io.spring.start.site.extension.dependency.springdocopenapi;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringDocOpenApiProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class SpringDocOpenApiProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	@Test
	void shouldDoNothingIfNotSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springdoc", "springdoc-openapi-starter-webmvc-ui")
			.doesNotHaveDependency("org.springdoc", "springdoc-openapi-starter-webflux-ui");
	}

	@Test
	void shouldAddWebMvcIfWebfluxIsNotAround() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "springdoc-openapi");
		assertThat(mavenPom(request)).hasDependency("org.springdoc", "springdoc-openapi-starter-webmvc-ui")
			.doesNotHaveDependency("org.springdoc", "springdoc-openapi-starter-webflux-ui")
			.hasDependency(getDependency("web"));
	}

	@Test
	void shouldUseWebfluxVersionIfWebfluxIsSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "webflux", "springdoc-openapi");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springdoc", "springdoc-openapi-starter-webmvc-ui")
			.hasDependency("org.springdoc", "springdoc-openapi-starter-webflux-ui");
	}

	@Test
	void shouldNotUseWebfluxVersionIfWebfluxAndWebMvcIsSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web", "webflux", "springdoc-openapi");
		assertThat(mavenPom(request)).hasDependency("org.springdoc", "springdoc-openapi-starter-webmvc-ui")
			.doesNotHaveDependency("org.springdoc", "springdoc-openapi-starter-webflux-ui");
	}

}
