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

package io.spring.start.site.extension.build.maven;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * A {@link HelpDocumentCustomizer} that adds reference links for Apache Maven.
 *
 * @author Jenn Strater
 * @author Stephane Nicoll
 */
class MavenBuildSystemHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final VersionRange SPRING_BOOT_2_3_OR_LATER = VersionParser.DEFAULT.parseRange("2.3.0.M1");

	private static final VersionRange NEW_DOC_STRUCTURE = VersionParser.DEFAULT.parseRange("2.3.0.M4");

	private final Version springBootVersion;

	private final boolean springBoot23;

	private final boolean newDocStructure;

	MavenBuildSystemHelpDocumentCustomizer(ProjectDescription description) {
		this.springBootVersion = description.getPlatformVersion();
		this.springBoot23 = SPRING_BOOT_2_3_OR_LATER.match(this.springBootVersion);
		this.newDocStructure = NEW_DOC_STRUCTURE.match(this.springBootVersion);
	}

	@Override
	public void customize(HelpDocument document) {
		document.gettingStarted().addReferenceDocLink("https://maven.apache.org/guides/index.html",
				"Official Apache Maven documentation");
		String referenceGuideUrl = generateReferenceGuideUrl();
		document.gettingStarted().addReferenceDocLink(referenceGuideUrl, "Spring Boot Maven Plugin Reference Guide");
		if (this.springBoot23) {
			String buildImageSection = referenceGuideUrl + "#build-image";
			document.gettingStarted().addReferenceDocLink(buildImageSection, "Create an OCI image");
		}
	}

	private String generateReferenceGuideUrl() {
		String baseUrlFormat = "https://docs.spring.io/spring-boot/docs/%s/maven-plugin/";
		if (this.springBoot23) {
			String location = (this.newDocStructure) ? "reference/html/" : "html/";
			baseUrlFormat = baseUrlFormat + location;
		}
		return String.format(baseUrlFormat, this.springBootVersion);
	}

}
