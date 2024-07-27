/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.build.gradle;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * A {@link HelpDocumentCustomizer} that adds reference links for Gradle.
 *
 * @author Jenn Strater
 * @author Stephane Nicoll
 */
class GradleBuildSystemHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final String SPRING_BOOT_DOCS_URL = "https://docs.spring.io/spring-boot";

	private static final VersionRange SPRING_BOOT_3_3_0_OR_LATER = VersionParser.DEFAULT.parseRange("3.3.0");

	private final Version springBootVersion;

	GradleBuildSystemHelpDocumentCustomizer(ProjectDescription description) {
		this.springBootVersion = description.getPlatformVersion();
	}

	@Override
	public void customize(HelpDocument document) {
		document.gettingStarted()
			.addAdditionalLink("https://scans.gradle.com#gradle",
					"Gradle Build Scans – insights for your project's build");
		document.gettingStarted().addReferenceDocLink("https://docs.gradle.org", "Official Gradle documentation");
		document.gettingStarted()
			.addReferenceDocLink(generateReferenceGuideUrl(), "Spring Boot Gradle Plugin Reference Guide");
		document.gettingStarted()
			.addReferenceDocLink(String.format(
					generateReferenceGuideUrl() + (shouldChangeUrl() ? "/packaging-oci-image.html" : "#build-image"),
					this.springBootVersion), "Create an OCI image");
	}

	private String generateReferenceGuideUrl() {
		String baseUrlFormat = SPRING_BOOT_DOCS_URL
				+ (shouldChangeUrl() ? "/%s/gradle-plugin" : "/%s/gradle-plugin/reference/html/");
		return String.format(baseUrlFormat, this.springBootVersion);
	}

	private boolean shouldChangeUrl() {

		return this.SPRING_BOOT_3_3_0_OR_LATER.match(this.springBootVersion);
	}

}
