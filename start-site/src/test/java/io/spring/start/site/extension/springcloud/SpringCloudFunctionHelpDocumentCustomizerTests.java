/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.springcloud;

import java.util.Arrays;
import java.util.List;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringCloudFunctionHelpDocumentCustomizer}.
 *
 * @author Olga Maciaszek-Sharma
 */
class SpringCloudFunctionHelpDocumentCustomizerTests extends AbstractExtensionTests {

	private static final String AWS_SECTION_TITLE = "# Additional build setup for Spring Cloud Function apps - AWS";

	private static final String AZURE_SECTION_TITLE = "# Additional build setup for Spring Cloud Function apps - Azure";

	@Test
	void functionBuildSetupInfoSectionAddedForMaven() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.2.0.M4");
		request.setType("maven-build");
		request.setDependencies(Arrays.asList("cloud-function", "cloud-aws", "azure-support"));
		List<String> lines = generateHelpDocument(request);
		assertThat(lines).contains(AWS_SECTION_TITLE);
		assertThat(lines).contains(AZURE_SECTION_TITLE);
	}

	@Test
	void functionBuildSetupInfoSectionAddedForGradle() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.2.0.M4");
		request.setType("gradle-build");
		request.setDependencies(Arrays.asList("cloud-function", "cloud-aws", "azure-support"));
		List<String> lines = generateHelpDocument(request);
		assertThat(lines).contains(AWS_SECTION_TITLE);
		assertThat(lines).contains(AZURE_SECTION_TITLE);
		assertThat(lines).contains("A GRADLE plugin has not been provided by Azure as of yet.");
	}

	@Test
	void functionBuildSetupInfoSectionNotAddedWhenRelevantVersionIsNotSelected() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.1.0.RELEASE");
		request.setDependencies(Arrays.asList("cloud-function", "cloud-aws", "azure-support"));
		List<String> lines = generateHelpDocument(request);
		assertThat(lines).doesNotContain(AWS_SECTION_TITLE);
		assertThat(lines).doesNotContain(AZURE_SECTION_TITLE);
	}

	@Test
	void functionBuildSetupInfoSectionNotAddedWhenFunctionAndCloudDependenciesAbsent() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.2.0.M4");
		assertThat(generateHelpDocument(request)).doesNotContain(AWS_SECTION_TITLE);
		assertThat(generateHelpDocument(request)).doesNotContain(AZURE_SECTION_TITLE);
	}

	@Test
	void functionBuildSetupInfoSectionNotAddedForFunctionSnapshot() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.2.0.BUILD_SNAPSHOT");
		request.setType("maven-build");
		request.setDependencies(Arrays.asList("cloud-function", "cloud-aws", "azure-support"));
		List<String> lines = generateHelpDocument(request);
		assertThat(lines).doesNotContain(AWS_SECTION_TITLE);
		assertThat(lines).doesNotContain(AZURE_SECTION_TITLE);
	}

	private List<String> generateHelpDocument(ProjectRequest projectRequest) {
		return generateProject(projectRequest).readAllLines("HELP.md");
	}

}
