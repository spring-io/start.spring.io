/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.build.maven;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MavenBuildSystemHelpDocumentCustomizer}.
 *
 * @author Jenn Strater
 * @author Andy Wilkinson
 * @author Moritz Halbritter
 */
class MavenBuildSystemHelpDocumentCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void linksAddedToHelpDocumentForMavenBuild() {
		assertHelpDocument("maven-build").contains(
				"* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)",
				"* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/%s/maven-plugin)"
					.formatted(BOOT_VERSION.getVersion()),
				"* [Create an OCI image](https://docs.spring.io/spring-boot/%s/maven-plugin/build-image.html)"
					.formatted(BOOT_VERSION.getVersion()));
	}

	@Test
	void linksNotAddedToHelpDocumentForGradleBuild() {
		assertHelpDocument("gradle-build").noneMatch((line) -> line.contains("Maven"));
	}

	private ListAssert<String> assertHelpDocument(String type) {
		ProjectRequest request = createProjectRequest("web");
		request.setType(type);
		return assertThat(helpDocument(request)).lines();
	}

}
