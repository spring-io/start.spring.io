/*
 * Copyright 2012-2024 the original author or authors.
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

package io.spring.start.site.extension.properties;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultApplicationPropertiesCustomizer}.
 *
 * @author Moritz Halbritter
 */
class DefaultApplicationPropertiesCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldAddSpringApplicationName() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("3.1.0");
		request.setJavaVersion("21");
		request.setName("test");
		assertApplicationProperties(request).lines().contains("spring.application.name=test");
	}

	private TextAssert assertApplicationProperties(ProjectRequest request) {
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("src/main/resources/application.properties"));
	}

}
