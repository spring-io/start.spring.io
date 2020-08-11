/*
 * Copyright 2012-2020 the original author or authors.
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

	private static final VersionRange SPRING_BOOT_2_3_OR_LATER = VersionParser.DEFAULT.parseRange("2.3.0.M1");

	private final Version springBootVersion;

	private final boolean buildImageAvailable;

	GradleBuildSystemHelpDocumentCustomizer(ProjectDescription description) {
		this.springBootVersion = description.getPlatformVersion();
		this.buildImageAvailable = SPRING_BOOT_2_3_OR_LATER.match(this.springBootVersion);
	}

	@Override
	public void customize(HelpDocument document) {
		document.gettingStarted().addAdditionalLink("https://scans.gradle.com#gradle",
				"Gradle Build Scans – insights for your project's build");
		document.gettingStarted().addReferenceDocLink("https://docs.gradle.org", "Official Gradle documentation");
		document.gettingStarted().addReferenceDocLink(
				String.format("https://docs.spring.io/spring-boot/docs/%s/gradle-plugin/reference/html/",
						this.springBootVersion),
				"Spring Boot Gradle Plugin Reference Guide");
		if (this.buildImageAvailable) {
			document.gettingStarted()
					.addReferenceDocLink(String.format(
							"https://docs.spring.io/spring-boot/docs/%s/gradle-plugin/reference/html/#build-image",
							this.springBootVersion), "Create an OCI image");
		}
	}

}
