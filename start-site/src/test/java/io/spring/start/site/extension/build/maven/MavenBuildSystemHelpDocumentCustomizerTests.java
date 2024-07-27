/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.build.maven;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MavenBuildSystemHelpDocumentCustomizer}.
 *
 * @author Jenn Strater
 * @author Andy Wilkinson
 */
class MavenBuildSystemHelpDocumentCustomizerTests extends AbstractExtensionTests {

	private static final String SPRING_BOOT_VERSION = "3.3.0";

	@Test
	void linksAddedToHelpDocumentForMavenBuild() {
		assertHelpDocument("maven-build", SPRING_BOOT_VERSION).contains(
				"* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)",
				"* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.0/maven-plugin)",
				"* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.0/maven-plugin/build-image.html)");
	}

	@Test
	void linksNotAddedToHelpDocumentForGradleBuild() {
		assertHelpDocument("gradle-build", SPRING_BOOT_VERSION).doesNotContain(
				"* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)",
				"* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.0/maven-plugin/)");
	}

	private ListAssert<String> assertHelpDocument(String type, String version) {
		ProjectRequest request = createProjectRequest("web");
		request.setType(type);
		request.setBootVersion(version);
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md")).lines();
	}

}
