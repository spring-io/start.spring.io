/*
 * Copyright 2012 - present the original author or authors.
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
