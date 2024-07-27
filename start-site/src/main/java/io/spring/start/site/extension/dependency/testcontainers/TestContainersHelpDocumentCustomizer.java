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
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;
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

	private static final VersionRange SPRING_BOOT_3_3_0_OR_LATER = VersionParser.DEFAULT.parseRange("3.3.0");

	private final ProjectDescription description;

	private final ServiceConnections serviceConnections;

	TestContainersHelpDocumentCustomizer(ProjectDescription description, ServiceConnections serviceConnections) {
		this.description = description;
		this.serviceConnections = serviceConnections;
	}

	@Override
	public void customize(HelpDocument document) {
		String referenceDocUrl = (shouldChangeUrl()
				? "https://docs.spring.io/spring-boot/%s/reference/testing/testcontainers.html#testing.testcontainers"
				: "https://docs.spring.io/spring-boot/docs/%s/reference/html/features.html#features.testing.testcontainers")
			.formatted(this.description.getPlatformVersion());
		document.gettingStarted().addReferenceDocLink(referenceDocUrl, "Spring Boot Testcontainers support");

		Map<String, Object> model = new HashMap<>();
		List<DockerService> dockerServices = this.serviceConnections.values()
			.map(ServiceConnection::dockerService)
			.toList();
		model.put("services", dockerServices);
		model.put("testcontainersAtDevelopmentTimeLink", (shouldChangeUrl()
				? "https://docs.spring.io/spring-boot/%s/reference/features/dev-services.html#features.dev-services.testcontainers"
				: "https://docs.spring.io/spring-boot/docs/%s/reference/html/features.html#features.testcontainers")
			.formatted(this.description.getPlatformVersion()));
		document.addSection("testcontainers", model);
	}

	private boolean shouldChangeUrl() {

		return this.SPRING_BOOT_3_3_0_OR_LATER.match(this.description.getPlatformVersion());
	}

}
