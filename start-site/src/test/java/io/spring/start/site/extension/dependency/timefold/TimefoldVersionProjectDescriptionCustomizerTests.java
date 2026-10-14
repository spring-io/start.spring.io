/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.timefold;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TimefoldVersionProjectDescriptionCustomizer}.
 *
 * @author Moritz Halbritter
 */
class TimefoldVersionProjectDescriptionCustomizerTests extends AbstractExtensionTests {

	@ParameterizedTest
	@ValueSource(strings = { "17", "18", "19", "20" })
	void java21IsRequiredWithBoot40(String jvmVersion) {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "timefold-solver");
		request.setJavaVersion(jvmVersion);
		assertThat(mavenPom(request)).hasProperty("java.version", "21");
	}

	@Test
	void warningAddedWhenJavaVersionIsRaised() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "timefold-solver");
		request.setJavaVersion("17");
		assertThat(helpDocument(request)).lines()
			.containsSubsequence("# Read Me First",
					"* The JVM level was changed to '21' as Timefold requires Java 21 or later.");
	}

	@ParameterizedTest
	@ValueSource(strings = { "21", "25", "27" })
	void java21OrLaterIsLeftAsIsWithBoot40(String jvmVersion) {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "timefold-solver");
		request.setJavaVersion(jvmVersion);
		assertThat(mavenPom(request)).hasProperty("java.version", jvmVersion);
	}

}
