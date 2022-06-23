/*
 * Copyright 2012-2021 the original author or authors.
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

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SpringAzureHelpDocumentCustomizer}.
 *
 * @author Yonghui Ye
 */
class SpringAzureHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void referenceSectionWithAzureSupportDependencyIsPresent() {
		assertHelpDocument("azure-support").contains("Azure Support");
	}

	@Test
	void referenceSectionWithAzureActiveDirectoryDependencyIsPresent() {
		assertHelpDocument("azure-active-directory").contains("Azure Active Directory");
	}

	@Test
	void referenceSectionWithAzureCosmosDependencyIsPresent() {
		assertHelpDocument("azure-cosmos-db").contains("Azure Cosmos DB");
	}

	@Test
	void referenceSectionWithAzureKeyVaultDependencyIsPresent() {
		assertHelpDocument("azure-keyvault").contains("Azure Key Vault secrets", "Azure Key Vault certificates");
	}

	@Test
	void referenceSectionWithAzureActuatorDependencyIsPresent() {
		assertHelpDocument("azure-support", "actuator").contains("Azure Actuator");
	}

	@Test
	void referenceSectionWithAzureNativeDependencyIsPresent() {
		assertHelpDocument("azure-storage", "integration", "native").contains("Azure Native");
	}

	@Test
	void referenceSectionWithAzureSleuthDependencyIsPresent() {
		assertHelpDocument("azure-support", "cloud-starter-sleuth").contains("Azure Sleuth");
	}

	@Test
	void referenceSectionWithAzureIntegrationStorageQueueDependencyIsPresent() {
		assertHelpDocument("azure-storage", "integration").contains("Azure Integration Storage Queue");
	}

	private TextAssert assertHelpDocument(String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setBootVersion("2.6.4");
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md"));
	}

}
