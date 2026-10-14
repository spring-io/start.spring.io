/*
 * Copyright 2012 - present the original author or authors.
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
