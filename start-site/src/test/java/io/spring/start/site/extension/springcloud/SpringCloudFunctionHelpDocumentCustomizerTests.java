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
 * Tests for {@link SpringCloudFunctionHelpDocumentCustomizer}
 *
 * @author Olga Maciaszek-Sharma
 */
class SpringCloudFunctionHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void functionBuildSetupInfoAddedForMaven() {
		ProjectRequest request = createProjectRequest();
		request.setType("maven-build");
		request.setDependencies(
				Arrays.asList("cloud-function", "cloud-aws", "azure-support"));
		List<String> lines = generateHelpDocument(request);
		assertThat(lines).contains(
				"# Additional build setup for Spring Cloud Function apps - AWS");
		assertThat(lines).contains(
				"# Additional build setup for Spring Cloud Function apps - Azure");
	}

	@Test
	void functionBuildSetupInfoAddedForGradle() {
		ProjectRequest request = createProjectRequest();
		request.setType("gradle-build");
		request.setDependencies(
				Arrays.asList("cloud-function", "cloud-aws", "azure-support"));
		List<String> lines = generateHelpDocument(request);
		assertThat(lines).contains(
				"# Additional build setup for Spring Cloud Function apps - AWS");
		assertThat(lines).doesNotContain(
				"# Additional build setup for Spring Cloud Function apps - Azure");
	}

	private List<String> generateHelpDocument(ProjectRequest projectRequest) {
		return generateProject(projectRequest).readAllLines("HELP.md");
	}

}
