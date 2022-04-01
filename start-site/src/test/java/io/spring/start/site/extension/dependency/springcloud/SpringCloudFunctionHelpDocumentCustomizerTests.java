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

package io.spring.start.site.extension.dependency.springcloud;

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

	private static final String AZURE_SECTION_TITLE = "## Running Spring Cloud Function applications on Microsoft Azure";

	@Test
	void functionBuildSetupInfoSectionAddedForMaven() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.5.0");
		request.setType("maven-build");
		request.setDependencies(Arrays.asList("cloud-function", "azure-support"));
		assertThat(generateProject(request)).textFile("HELP.md").contains(AZURE_SECTION_TITLE);
	}

	@Test
	void functionBuildSetupInfoSectionAddedForGradle() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.5.0");
		request.setType("gradle-build");
		request.setDependencies(Arrays.asList("cloud-function", "azure-support"));
		assertThat(generateProject(request)).textFile("HELP.md").contains(AZURE_SECTION_TITLE);
	}

	@Test
	void functionBuildSetupInfoSectionNotAddedWhenFunctionAndCloudDependenciesAbsent() {
		ProjectRequest request = createProjectRequest();
		assertThat(generateProject(request)).textFile("HELP.md").doesNotContain(AZURE_SECTION_TITLE);
	}

}
