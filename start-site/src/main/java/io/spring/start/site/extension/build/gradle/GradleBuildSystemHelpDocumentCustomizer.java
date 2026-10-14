/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.build.gradle;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;

/**
 * A {@link HelpDocumentCustomizer} that adds reference links for Gradle.
 *
 * @author Jenn Strater
 * @author Stephane Nicoll
 */
class GradleBuildSystemHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final String SPRING_BOOT_DOCS_URL = "https://docs.spring.io/spring-boot";

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
			.addReferenceDocLink(generateReferenceGuideUrl() + "/packaging-oci-image.html", "Create an OCI image");
	}

	private String generateReferenceGuideUrl() {
		String baseUrlFormat = SPRING_BOOT_DOCS_URL + "/%s/gradle-plugin";
		return baseUrlFormat.formatted(this.springBootVersion);
	}

}
