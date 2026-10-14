/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.build.maven;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;

/**
 * A {@link HelpDocumentCustomizer} that adds reference links for Apache Maven.
 *
 * @author Jenn Strater
 * @author Stephane Nicoll
 */
class MavenBuildSystemHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final String SPRING_BOOT_DOCS_URL = "https://docs.spring.io/spring-boot";

	private final Version springBootVersion;

	MavenBuildSystemHelpDocumentCustomizer(ProjectDescription description) {
		this.springBootVersion = description.getPlatformVersion();
	}

	@Override
	public void customize(HelpDocument document) {
		document.gettingStarted()
			.addReferenceDocLink("https://maven.apache.org/guides/index.html", "Official Apache Maven documentation");
		String referenceGuideUrl = generateReferenceGuideUrl();
		document.gettingStarted().addReferenceDocLink(referenceGuideUrl, "Spring Boot Maven Plugin Reference Guide");
		String buildImageSection = referenceGuideUrl + "/build-image.html";
		document.gettingStarted().addReferenceDocLink(buildImageSection, "Create an OCI image");
	}

	private String generateReferenceGuideUrl() {
		String baseUrlFormat = SPRING_BOOT_DOCS_URL + "/%s/maven-plugin";
		return baseUrlFormat.formatted(this.springBootVersion);
	}

}
