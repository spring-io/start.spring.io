/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.htmx;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HtmxBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class HtmxBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	private final Dependency htmx = Dependency.withId("htmx", "io.github.wimdeblauwe", "htmx-spring-boot");

	private final Dependency htmxThymeleaf = Dependency.withId("htmx", "io.github.wimdeblauwe",
			"htmx-spring-boot-thymeleaf");

	@Test
	void shouldUseHtmxThymleafIfThymeleafIsSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "htmx", "thymeleaf");
		assertThat(mavenPom(request)).doesNotHaveDependency(this.htmx.getGroupId(), this.htmx.getArtifactId())
			.hasDependency(this.htmxThymeleaf);
	}

	@Test
	void shouldUsePlainHtmxIfThymeleafIsNotSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "htmx");
		assertThat(mavenPom(request))
			.doesNotHaveDependency(this.htmxThymeleaf.getGroupId(), this.htmxThymeleaf.getArtifactId())
			.hasDependency(this.htmx);
	}

	@Test
	void shouldNotAddHtmx() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).doesNotHaveDependency(this.htmx.getGroupId(), this.htmx.getArtifactId())
			.doesNotHaveDependency(this.htmxThymeleaf.getGroupId(), this.htmxThymeleaf.getArtifactId());
	}

}
