/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.okta;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OktaHelpDocumentCustomizer}.
 *
 * @author Stephane Nicoll
 */
class OktaHelpDocumentCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void linksAddedToHelpDocumentForGradleBuild() {
		assertHelpDocument("gradle-build", "okta").contains("## OAuth 2.0 and OIDC with Okta",
				"If you don't have a free Okta developer account, you can create one with [the Okta CLI](https://cli.okta.com):");
	}

	@Test
	void linksAddedToHelpDocumentForMavenBuild() {
		assertHelpDocument("maven-build", "okta").contains("## OAuth 2.0 and OIDC with Okta",
				"If you don't have a free Okta developer account, you can create one with [the Okta CLI](https://cli.okta.com):");
	}

	@Test
	void linksNotAddedToHelpDocumentForBuildWithoutOkta() {
		assertHelpDocument("gradle-build").noneMatch((line) -> line.contains("Okta"));
	}

	private org.assertj.core.api.ListAssert<String> assertHelpDocument(String type, String... dependencies) {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, dependencies);
		request.setType(type);
		return assertThat(helpDocument(request)).lines();
	}

}
