/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that Getting Started Guides.
 *
 * @author Yonghui Ye
 */
class SpringAzureHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final String AZURE_ACTUATOR_ARTIFACT_ID = "spring-cloud-azure-starter-actuator";

	private static final String AZURE_SLEUTH_ARTIFACT_ID = "spring-cloud-azure-trace-sleuth";

	private static final String AZURE_INTEGRATION_STORAGE_QUEUE_ARTIFACT_ID = "spring-cloud-azure-starter-integration-storage-queue";

	private final Build build;

	SpringAzureHelpDocumentCustomizer(Build build) {
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		addSpringDocsLink(document, AZURE_ACTUATOR_ARTIFACT_ID, "https://aka.ms/spring/docs/actuator",
				"Azure Actuator");
		addSpringDocsLink(document, AZURE_SLEUTH_ARTIFACT_ID, "https://aka.ms/spring/docs/sleuth", "Azure Sleuth");
		addSpringDocsLink(document, AZURE_INTEGRATION_STORAGE_QUEUE_ARTIFACT_ID,
				"https://aka.ms/spring/docs/spring-integration/storage-queue", "Azure Integration Storage Queue");
	}

	private void addSpringDocsLink(HelpDocument document, String dependencyId, String href, String description) {
		if (this.build.dependencies().items().anyMatch((u) -> u.getArtifactId().equals(dependencyId))) {
			document.gettingStarted().addReferenceDocLink(href, description);
		}
	}

}
