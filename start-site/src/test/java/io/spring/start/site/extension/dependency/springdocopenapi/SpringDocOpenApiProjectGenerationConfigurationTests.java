/*
 * Copyright 2012 - present the original author or authors.
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

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_1;

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
