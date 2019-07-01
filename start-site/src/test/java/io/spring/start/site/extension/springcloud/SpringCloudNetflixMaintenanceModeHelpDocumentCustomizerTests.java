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
import java.util.Collections;
import java.util.List;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringCloudNetflixMaintenanceModeHelpDocumentCustomizer}
 *
 * @author Olga Maciaszek-Sharma
 * @author Stephane Nicoll
 */
class SpringCloudNetflixMaintenanceModeHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void sectionAddedWhenMaintenanceModeDependenciesPresent() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.1.5.RELEASE");
		request.setDependencies(Arrays.asList("cloud-ribbon", "cloud-eureka"));
		List<String> lines = generateHelpDocument(request);
		assertThat(lines).containsSequence("# Spring Cloud Netflix Maintenance Mode", "",
				"The dependencies listed below are in maintenance mode. We do not recommend adding them to",
				"new projects:", "", "*  Ribbon", "");
	}

	@Test
	void sectionNotAddedWhenRelevantVersionIsNotSelected() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.0.7.RELEASE");
		request.setDependencies(Arrays.asList("cloud-ribbon", "cloud-eureka"));
		List<String> lines = generateHelpDocument(request);
		assertThat(lines).doesNotContain("# Spring Cloud Netflix Maintenance Mode");
	}

	@Test
	void sectionNotAddedWhenNoMaintenanceModeDependencies() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.1.5.RELEASE");
		request.setDependencies(Collections.singletonList("cloud-eureka"));
		List<String> lines = generateHelpDocument(request);
		assertThat(lines).doesNotContain("# Spring Cloud Netflix Maintenance Mode");
	}

	private List<String> generateHelpDocument(ProjectRequest projectRequest) {
		return generateProject(projectRequest).readAllLines("HELP.md");
	}

}
