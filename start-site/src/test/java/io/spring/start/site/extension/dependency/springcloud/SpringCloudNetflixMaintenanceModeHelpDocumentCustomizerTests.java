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
import java.util.Collections;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

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
		request.setBootVersion("2.3.0.RELEASE");
		request.setDependencies(Arrays.asList("cloud-ribbon", "cloud-eureka"));
		assertHelpDocument(request).containsSequence("# Spring Cloud Netflix Maintenance Mode", "",
				"The dependencies listed below are in maintenance mode. We do not recommend adding them to",
				"new projects:", "", "*  Ribbon", "");
	}

	@Test
	void sectionNotAddedWhenNoMaintenanceModeDependencies() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.3.0.RELEASE");
		request.setDependencies(Collections.singletonList("cloud-eureka"));
		assertHelpDocument(request).doesNotContain("# Spring Cloud Netflix Maintenance Mode");
	}

	private ListAssert<String> assertHelpDocument(ProjectRequest projectRequest) {
		ProjectStructure project = generateProject(projectRequest);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md")).lines();
	}

}
