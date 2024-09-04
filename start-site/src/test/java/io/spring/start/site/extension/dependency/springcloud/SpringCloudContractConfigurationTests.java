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

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Spring Cloud Contract configuration.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 */
class SpringCloudContractConfigurationTests extends AbstractExtensionTests {

	private static final String SPRING_BOOT_VERSION = "3.3.0";

	@Test
	void contractsDirectoryWithMavenIsCreatedWithSpringCloudContractVerifier() {
		ProjectRequest request = createProjectRequest("web", "cloud-contract-verifier");
		request.setBootVersion(SPRING_BOOT_VERSION);
		request.setType("maven-project");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/test/resources/contracts")).exists().isDirectory();
	}

	@Test
	void contractsDirectoryWithMavenIsNotCreatedIfSpringCloudContractVerifierIsNotRequested() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion(SPRING_BOOT_VERSION);
		request.setType("maven-project");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/test/resources/contracts")).doesNotExist();
	}

	@Test
	void contractsDirectoryWithGradleIsCreatedWithSpringCloudContractVerifier() {
		ProjectRequest request = createProjectRequest("web", "cloud-contract-verifier");
		request.setBootVersion(SPRING_BOOT_VERSION);
		request.setType("gradle-project");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/contractTest/resources/contracts")).exists()
			.isDirectory();
	}

	@Test
	void contractsDirectoryWithGradleIsNotCreatedIfSpringCloudContractVerifierIsNotRequested() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion(SPRING_BOOT_VERSION);
		request.setType("gradle-project");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/contractTest/resources/contracts")).doesNotExist();
	}

}
