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
 * Tests for {@link SpringCloudConfigImportHelpDocumentCustomizer}.
 *
 * @author Olga Maciaszek-Sharma
 */
class SpringCloudConfigImportHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void sectionAddedWhenConfigClientDependenciesPresent() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.4.0");
		request.setDependencies(Arrays.asList("cloud-config-client", "cloud-starter-consul-config",
				"cloud-starter-vault-config", "cloud-starter-zookeeper-config"));
		assertHelpDocument(request).contains("# Spring Boot Config Data Import",
				"`spring.config.import=optional:vault:`", "`spring.config.import=optional:consul:`",
				"`spring.config.import=optional:configserver:`", "`spring.config.import=optional:zookeeper:`");
	}

	@Test
	void sectionNotAddedWhenNoConfigClientDependencies() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.4.0");
		request.setDependencies(Collections.singletonList("cloud-config-server"));
		assertHelpDocument(request).doesNotContain("# Spring Boot Config Data Import");
	}

	@Test
	void sectionNotAddedWhenOlderSpringBootVersion() {
		ProjectRequest request = createProjectRequest();
		request.setBootVersion("2.3.9.RELEASE");
		request.setDependencies(Arrays.asList("cloud-config-client", "cloud-starter-consul-config",
				"cloud-starter-vault-config", "cloud-starter-zookeeper-config"));
		assertHelpDocument(request).doesNotContain("# Spring Boot Config Data Import");
	}

	private ListAssert<String> assertHelpDocument(ProjectRequest projectRequest) {
		ProjectStructure project = generateProject(projectRequest);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md")).lines();
	}

}
