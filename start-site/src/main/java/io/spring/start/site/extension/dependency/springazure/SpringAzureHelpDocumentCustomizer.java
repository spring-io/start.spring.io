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
public class SpringAzureHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final String AZURE_DEPENDENCY_ID = "azure-support";

	private static final String AZURE_ACTIVE_DIRECTORY_DEPENDENCY_ID = "azure-active-directory";

	private static final String AZURE_COSMOS_DEPENDENCY_ID = "azure-cosmos-db";

	private static final String AZURE_KEY_VAULT_DEPENDENCY_ID = "azure-keyvault";

	private static final String AZURE_STORAGE_DEPENDENCY_ID = "azure-storage";

	private static final String AZURE_ACTUATOR_ARTIFACT_ID = "spring-cloud-azure-starter-actuator";

	private static final String AZURE_SLEUTH_ARTIFACT_ID = "spring-cloud-azure-trace-sleuth";

	private static final String AZURE_INTEGRATION_STORAGE_QUEUE_ARTIFACT_ID = "spring-cloud-azure-starter-integration-storage-queue";

	private final Build build;

	public SpringAzureHelpDocumentCustomizer(Build build) {
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		if (this.build.dependencies().has(AZURE_DEPENDENCY_ID)) {
			document.gettingStarted().addGuideLink(
					"https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/spring-boot-starters-for-azure",
					"Spring Cloud on Azure");
		}

		if (this.build.dependencies().has(AZURE_ACTIVE_DIRECTORY_DEPENDENCY_ID)) {
			document.gettingStarted().addGuideLink(
					"https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-active-directory",
					"Azure Active Directory");
		}

		if (this.build.dependencies().has(AZURE_COSMOS_DEPENDENCY_ID)) {
			document.gettingStarted().addGuideLink(
					"https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-cosmos-db",
					"Azure Cosmos DB");
		}

		if (this.build.dependencies().has(AZURE_KEY_VAULT_DEPENDENCY_ID)) {
			document.gettingStarted().addGuideLink(
					"https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-key-vault",
					"Azure Key Vault secrets");
			document.gettingStarted().addGuideLink(
					"https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-key-vault-certificates",
					"Azure Key Vault certificates");
		}

		if (this.build.dependencies().has(AZURE_STORAGE_DEPENDENCY_ID)) {
			document.gettingStarted().addGuideLink(
					"https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-storage",
					"Azure Storage");
		}

		if (this.build.dependencies().items().anyMatch((u) -> u.getArtifactId().equals(AZURE_ACTUATOR_ARTIFACT_ID))) {
			document.gettingStarted().addReferenceDocLink(
					"https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#enable-health-indicator",
					"Azure Actuator");
		}

		if (this.build.dependencies().items().anyMatch((u) -> u.getArtifactId().equals(AZURE_SLEUTH_ARTIFACT_ID))) {
			document.gettingStarted().addReferenceDocLink(
					"https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#enable-sleuth",
					"Azure Sleuth");
		}

		if (this.build.dependencies().items()
				.anyMatch((u) -> u.getArtifactId().equals(AZURE_INTEGRATION_STORAGE_QUEUE_ARTIFACT_ID))) {
			document.gettingStarted().addReferenceDocLink(
					"https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#spring-integration-with-azure-storage-queue",
					"Azure Integration Storage Queue");
		}
	}

}
