/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAzureMavenBuildCustomizer}.
 *
 * @author Muyao Feng
 * @author Moritz Halbritter
 */
class SpringAzureMavenBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	@Test
	void shouldDoNothingIfAzureSupportIsntSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web");
		assertThat(mavenPom(request)).doesNotContain("azure-container-apps-maven-plugin");
	}

	@Test
	void azureContainerAppsMavenPluginAddedWhenAzureSupportPresent() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "azure-support");
		assertThat(mavenPom(request)).hasText("/project/build/plugins/plugin[1]/groupId", "com.microsoft.azure")
			.hasText("/project/build/plugins/plugin[1]/artifactId", "azure-container-apps-maven-plugin");
	}

}
