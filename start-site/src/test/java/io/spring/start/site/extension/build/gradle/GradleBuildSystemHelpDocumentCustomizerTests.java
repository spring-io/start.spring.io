/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.build.gradle;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GradleBuildSystemHelpDocumentCustomizer}.
 *
 * @author Jenn Strater
 * @author Andy Wilkinson
 * @author Moritz Halbritter
 */
class GradleBuildSystemHelpDocumentCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void linksAddedToHelpDocumentForGradleBuild() {
		assertHelpDocument("gradle-build").contains("* [Official Gradle documentation](https://docs.gradle.org)",
				"* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)",
				"* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/%s/gradle-plugin)"
					.formatted(BOOT_VERSION.getVersion()),
				"* [Create an OCI image](https://docs.spring.io/spring-boot/%s/gradle-plugin/packaging-oci-image.html)"
					.formatted(BOOT_VERSION.getVersion()));
	}

	@Test
	void linksNotAddedToHelpDocumentForMavenBuild() {
		assertHelpDocument("maven-build").noneMatch((line) -> line.contains("Gradle"));
	}

	private ListAssert<String> assertHelpDocument(String type) {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web");
		request.setType(type);
		return assertThat(helpDocument(request)).lines();
	}

}
