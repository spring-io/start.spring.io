/*
 * Copyright 2012 - present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V3_5;

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
