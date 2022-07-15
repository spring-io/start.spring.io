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

	private static final String AZURE_DEPENDENCY_ID = "azure-support";

	private static final String AZURE_ACTIVE_DIRECTORY_DEPENDENCY_ID = "azure-active-directory";

	private static final String AZURE_COSMOS_DEPENDENCY_ID = "azure-cosmos-db";

	private static final String AZURE_KEY_VAULT_DEPENDENCY_ID = "azure-keyvault";

	private static final String AZURE_STORAGE_DEPENDENCY_ID = "azure-storage";

	private static final String AZURE_ACTUATOR_ARTIFACT_ID = "spring-cloud-azure-starter-actuator";

	private static final String AZURE_SLEUTH_ARTIFACT_ID = "spring-cloud-azure-trace-sleuth";

	private static final String AZURE_INTEGRATION_STORAGE_QUEUE_ARTIFACT_ID = "spring-cloud-azure-starter-integration-storage-queue";

	private final Build build;

	SpringAzureHelpDocumentCustomizer(Build build) {
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		addMsDocsLink(document, AZURE_DEPENDENCY_ID, "https://aka.ms/spring/msdocs", "Spring Cloud Azure");
		addMsDocsLink(document, AZURE_ACTIVE_DIRECTORY_DEPENDENCY_ID, "https://aka.ms/spring/msdocs/aad",
				"Azure Active Directory");
		addMsDocsLink(document, AZURE_COSMOS_DEPENDENCY_ID, "https://aka.ms/spring/msdocs/cosmos", "Azure Cosmos DB");
		addMsDocsLink(document, AZURE_KEY_VAULT_DEPENDENCY_ID, "https://aka.ms/spring/msdocs/keyvault",
				"Azure Key Vault secrets");
		addMsDocsLink(document, AZURE_KEY_VAULT_DEPENDENCY_ID, "https://aka.ms/spring/msdocs/keyvault/certificates",
				"Azure Key Vault certificates");
		addMsDocsLink(document, AZURE_STORAGE_DEPENDENCY_ID, "https://aka.ms/spring/msdocs/storage", "Azure Storage");
		addSpringDocsLink(document, AZURE_ACTUATOR_ARTIFACT_ID, "https://aka.ms/spring/docs/actuator",
				"Azure Actuator");
		addSpringDocsLink(document, AZURE_SLEUTH_ARTIFACT_ID, "https://aka.ms/spring/docs/sleuth", "Azure Sleuth");
		addSpringDocsLink(document, AZURE_INTEGRATION_STORAGE_QUEUE_ARTIFACT_ID,
				"https://aka.ms/spring/docs/spring-integration/storage-queue", "Azure Integration Storage Queue");
	}

	private void addMsDocsLink(HelpDocument document, String dependencyId, String href, String description) {
		if (this.build.dependencies().has(dependencyId)) {
			document.gettingStarted().addGuideLink(href, description);
		}
	}

	private void addSpringDocsLink(HelpDocument document, String dependencyId, String href, String description) {
		if (this.build.dependencies().items().anyMatch((u) -> u.getArtifactId().equals(dependencyId))) {
			document.gettingStarted().addReferenceDocLink(href, description);
		}
	}

}
