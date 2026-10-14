/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springcloud;

import java.util.Arrays;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringCloudFunctionHelpDocumentCustomizer}.
 *
 * @author Olga Maciaszek-Sharma
 */
class SpringCloudFunctionHelpDocumentCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	private static final String AZURE_SECTION_TITLE = "## Running Spring Cloud Function applications on Microsoft Azure";

	@Test
	void functionBuildSetupInfoSectionAddedForMaven() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION);
		request.setType("maven-build");
		request.setDependencies(Arrays.asList("cloud-function", "azure-support"));
		assertThat(helpDocument(request)).contains(AZURE_SECTION_TITLE);
	}

	@Test
	void functionBuildSetupInfoSectionAddedForGradle() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION);
		request.setType("gradle-build");
		request.setDependencies(Arrays.asList("cloud-function", "azure-support"));
		assertThat(helpDocument(request)).contains(AZURE_SECTION_TITLE);
	}

	@Test
	void functionBuildSetupInfoSectionNotAddedWhenFunctionAndCloudDependenciesAbsent() {
		ProjectRequest request = createProjectRequest();
		assertThat(helpDocument(request)).doesNotContain(AZURE_SECTION_TITLE);
	}

}
