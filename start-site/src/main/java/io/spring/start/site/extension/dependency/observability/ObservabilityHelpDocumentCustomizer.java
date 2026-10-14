/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;

/**
 * {@link HelpDocumentCustomizer} implementation for Observability.
 *
 * @author Stephane Nicoll
 */
public class ObservabilityHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final Version platformVersion;

	private final Build build;

	public ObservabilityHelpDocumentCustomizer(ProjectDescription description, Build build) {
		this.platformVersion = description.getPlatformVersion();
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		if (this.build.dependencies().has("distributed-tracing") || this.build.dependencies().has("zipkin")
				|| this.build.dependencies().has("opentelemetry")) {
			document.gettingStarted()
				.addReferenceDocLink("https://docs.micrometer.io/tracing/reference/index.html",
						"Distributed Tracing Reference Guide");
			document.gettingStarted()
				.addReferenceDocLink("https://docs.spring.io/spring-boot/%s/reference/actuator/tracing.html"
					.formatted(this.platformVersion), "Getting Started with Distributed Tracing");
		}
	}

}
