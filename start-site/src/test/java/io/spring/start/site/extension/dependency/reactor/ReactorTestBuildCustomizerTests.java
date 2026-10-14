/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.reactor;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ReactorTestBuildCustomizer}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class ReactorTestBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void shouldNotAddTestDependencyWithoutReactiveFacet() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.projectreactor", "reactor-test");
	}

	@Test
	void shouldNotAddTestDependency() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "webflux");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.projectreactor", "reactor-test");
	}

	@Test
	void shouldAddTestDependencyIfNonBootStarterIsUsed() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "cloud-gateway-reactive");
		assertThat(mavenPom(request)).hasDependency("io.projectreactor", "reactor-test", null, Dependency.SCOPE_TEST);
	}

}
