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

	private static final String AWS_SECTION_TITLE = "## Running Spring Cloud Function applications on AWS Lambda";

	private static final String AZURE_SECTION_TITLE = "## Running Spring Cloud Function applications on Microsoft Azure";

	@Test
	void functionBuildSetupInfoSectionAddedForMaven() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.2.0.M4");
		request.setType("maven-build");
		request.setDependencies(Arrays.asList("cloud-function", "cloud-aws", "azure-support"));
		assertThat(generateProject(request)).textFile("HELP.md").contains(AWS_SECTION_TITLE)
				.contains(AZURE_SECTION_TITLE);
	}

	@Test
	void functionBuildSetupInfoSectionAddedForGradle() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.2.0.M4");
		request.setType("gradle-build");
		request.setDependencies(Arrays.asList("cloud-function", "cloud-aws", "azure-support"));
		assertThat(generateProject(request)).textFile("HELP.md").contains(AWS_SECTION_TITLE)
				.contains(AZURE_SECTION_TITLE)
				.contains("A gradle plugin has not been provided by Microsoft Azure as yet.");
	}

	@Test
	void functionBuildSetupInfoSectionNotAddedWhenRelevantVersionIsNotSelected() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.1.0.RELEASE");
		request.setDependencies(Arrays.asList("cloud-function", "cloud-aws", "azure-support"));
		assertThat(generateProject(request)).textFile("HELP.md").doesNotContain(AWS_SECTION_TITLE)
				.doesNotContain(AZURE_SECTION_TITLE);
	}

	@Test
	void functionBuildSetupInfoSectionNotAddedWhenFunctionAndCloudDependenciesAbsent() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.2.0.M4");
		assertThat(generateProject(request)).textFile("HELP.md").doesNotContain(AWS_SECTION_TITLE)
				.doesNotContain(AZURE_SECTION_TITLE);
	}

	@Test
	void functionBuildSetupInfoSectionNotAddedForFunctionSnapshot() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.2.0.BUILD_SNAPSHOT");
		request.setType("maven-build");
		request.setDependencies(Arrays.asList("cloud-function", "cloud-aws", "azure-support"));
		assertThat(generateProject(request)).textFile("HELP.md").doesNotContain(AWS_SECTION_TITLE)
				.doesNotContain(AZURE_SECTION_TITLE);
	}

}
