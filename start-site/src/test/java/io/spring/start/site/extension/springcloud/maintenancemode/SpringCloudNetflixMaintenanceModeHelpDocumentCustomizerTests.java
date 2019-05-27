/*
 * Copyright 2019-2019 the original author or authors.
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

package io.spring.start.site.extension.springcloud.maintenancemode;

import java.util.Arrays;
import java.util.Collections;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringCloudNetflixMaintenanceModeHelpDocumentCustomizer}
 *
 * @author Olga Maciaszek-Sharma
 */
class SpringCloudNetflixMaintenanceModeHelpDocumentCustomizerTests
		extends AbstractExtensionTests {

	private static final String maintenanceModuleInfo = "# Warning - Maintenance Mode\n"
			+ "\n"
			+ "The dependencies listed below are in maintenance mode. We do not recommend adding them to\n"
			+ "new projects:\n" + "\n" + "    *  Ribbon";

	@Test
	void sectionAddedWhenMaintenanceModeDependenciesPresent() {
		ProjectRequest projectRequest = createProjectRequest();
		projectRequest.setDependencies(Arrays.asList("cloud-ribbon", "cloud-eureka"));
		String helpDocumentContent = getHelpDocumentContent(projectRequest);
		assertThat(helpDocumentContent).contains(maintenanceModuleInfo);
	}

	@Test
	void sectionNotAddedWhenNoMaintenanceModeDependencies() {
		ProjectRequest projectRequest = createProjectRequest();
		projectRequest.setDependencies(Collections.singletonList("cloud-eureka"));
		String helpDocumentContent = getHelpDocumentContent(projectRequest);
		assertThat(helpDocumentContent).doesNotContain("# Warning - Maintenance Mode");
	}

	private String getHelpDocumentContent(ProjectRequest projectRequest) {
		return String.join(System.lineSeparator(),
				generateProject(projectRequest).readAllLines("HELP.md"));
	}

}
