/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import java.util.List;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MavenBuildSystemHelpDocumentCustomizer}.
 *
 * @author Jenn Strater
 */
class MavenBuildSystemHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void linksAddedToHelpDocument() {
		ProjectRequest request = createProjectRequest("web");
		request.setType("maven-build");
		ProjectStructure structure = generateProject(request);
		List<String> lines = structure.readAllLines("HELP.md");
		assertThat(lines).contains(
				"* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)",
				"* [Free, shareable build insights for Gradle and Maven builds](https://scans.gradle.com#maven)");
		assertThat(lines).doesNotContain(
				"* [Official Gradle documentation](https://docs.gradle.org)",
				"* [Free, shareable build insights for Gradle and Maven builds](https://scans.gradle.com#gradle)");

	}

}
