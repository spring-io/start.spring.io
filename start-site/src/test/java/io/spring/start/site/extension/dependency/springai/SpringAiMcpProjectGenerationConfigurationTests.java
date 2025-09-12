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

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAiMcpProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 * @author Ilayaperumal Gopinathan
 */
class SpringAiMcpProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void shouldUseMvcServerIfWebMvcIsSelected() {
		ProjectRequest project = createProject("web", "spring-ai-mcp-server");
		assertThat(mavenPom(project)).hasDependency("org.springframework.ai", "spring-ai-starter-mcp-server-webmvc");
	}

	@Test
	void shouldUseWebFluxServerIfWebFluxIsSelected() {
		ProjectRequest project = createProject("webflux", "spring-ai-mcp-server");
		assertThat(mavenPom(project)).hasDependency("org.springframework.ai", "spring-ai-starter-mcp-server-webflux");
	}

	@Test
	void shouldUseStandardServerIfNeitherWebMvcNorWebFluxIsSelected() {
		ProjectRequest project = createProject("spring-ai-mcp-server");
		assertThat(mavenPom(project)).hasDependency("org.springframework.ai", "spring-ai-starter-mcp-server");
	}

	@Test
	void shouldUseWebFluxClientIfWebfluxIsSelected() {
		ProjectRequest project = createProject("webflux", "spring-ai-mcp-client");
		assertThat(mavenPom(project)).hasDependency("org.springframework.ai", "spring-ai-starter-mcp-client-webflux");
	}

	@Test
	void shouldUseStandardClientIfWebFluxIsNotSelected() {
		ProjectRequest project = createProject("spring-ai-mcp-client");
		assertThat(mavenPom(project)).hasDependency("org.springframework.ai", "spring-ai-starter-mcp-client");
	}

	private ProjectRequest createProject(String... styles) {
		ProjectRequest projectRequest = createProjectRequest(BOOT_VERSION, styles);
		projectRequest.setLanguage("java");
		return projectRequest;
	}

}
