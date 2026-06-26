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

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringRestDocsBuildCustomizer}.
 *
 * @author Andy Wilkinson
 */
class SpringRestDocsBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void shouldNotUseWebTestClientWithWebMvc() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web", "restdocs");
		assertThat(mavenPom(request))
			.hasDependency("org.springframework.restdocs", "spring-restdocs-mockmvc", null, "test")
			.hasDependency("org.springframework.boot", "spring-boot-starter-restdocs", null, "test")
			.doesNotHaveDependency("org.springframework.restdocs", "spring-restdocs-webtestclient");
	}

	@Test
	void shouldUseWebTestClientWithWebflux() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "webflux", "restdocs");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.restdocs", "spring-restdocs-mockmvc")
			.hasDependency("org.springframework.boot", "spring-boot-starter-restdocs", null, "test")
			.hasDependency("org.springframework.restdocs", "spring-restdocs-webtestclient", null, "test");
	}

	@Test
	void shouldUseWebTestClientWithJersey() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "jersey", "restdocs");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.restdocs", "spring-restdocs-mockmvc")
			.hasDependency("org.springframework.boot", "spring-boot-starter-restdocs", null, "test")
			.hasDependency("org.springframework.restdocs", "spring-restdocs-webtestclient", null, "test");
	}

}
