/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.testcontainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.start.site.container.DockerService;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;

/**
 * A {@link HelpDocumentCustomizer} that provide additional information about the
 * Testcontainers that are defined for the project.
 *
 * @author Moritz Halbritter
 */
class TestContainersHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final ProjectDescription description;

	private final ServiceConnections serviceConnections;

	TestContainersHelpDocumentCustomizer(ProjectDescription description, ServiceConnections serviceConnections) {
		this.description = description;
		this.serviceConnections = serviceConnections;
	}

	@Override
	public void customize(HelpDocument document) {
		String referenceDocUrl = "https://docs.spring.io/spring-boot/%s/reference/testing/testcontainers.html#testing.testcontainers"
			.formatted(this.description.getPlatformVersion());
		document.gettingStarted().addReferenceDocLink(referenceDocUrl, "Spring Boot Testcontainers support");
		Map<String, Object> model = new HashMap<>();
		List<DockerService> dockerServices = this.serviceConnections.values()
			.map(ServiceConnection::dockerService)
			.toList();
		model.put("services", dockerServices);
		model.put("testcontainersAtDevelopmentTimeLink",
				"https://docs.spring.io/spring-boot/%s/reference/features/dev-services.html#features.dev-services.testcontainers"
					.formatted(this.description.getPlatformVersion()));
		document.addSection("testcontainers", model);
	}

}
