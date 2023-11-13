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
		String referenceDocUrl = "https://docs.spring.io/spring-boot/docs/%s/reference/html/features.html#features.testing.testcontainers"
			.formatted(this.description.getPlatformVersion());
		document.gettingStarted().addReferenceDocLink(referenceDocUrl, "Spring Boot Testcontainers support");

		Map<String, Object> model = new HashMap<>();
		List<DockerService> dockerServices = this.serviceConnections.values()
			.map(ServiceConnection::dockerService)
			.toList();
		model.put("services", dockerServices);
		model.put("testcontainersAtDevelopmentTimeLink",
				"https://docs.spring.io/spring-boot/docs/%s/reference/html/features.html#features.testing.testcontainers.at-development-time"
					.formatted(this.description.getPlatformVersion()));
		document.addSection("testcontainers", model);
	}

}
