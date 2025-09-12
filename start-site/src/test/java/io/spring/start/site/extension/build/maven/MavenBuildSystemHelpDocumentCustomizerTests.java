/*
 * Copyright 2012 - present the original author or authors.
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

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MavenBuildSystemHelpDocumentCustomizer}.
 *
 * @author Jenn Strater
 * @author Andy Wilkinson
 * @author Moritz Halbritter
 */
class MavenBuildSystemHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void linksAddedToHelpDocumentForMavenBuild() {
		assertHelpDocument("maven-build").contains(
				"* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)",
				"* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.0/maven-plugin)",
				"* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.0/maven-plugin/build-image.html)");
	}

	@Test
	void linksNotAddedToHelpDocumentForGradleBuild() {
		assertHelpDocument("gradle-build").noneMatch((line) -> line.contains("Maven"));
	}

	private ListAssert<String> assertHelpDocument(String type) {
		ProjectRequest request = createProjectRequest("web");
		request.setType(type);
		return assertThat(helpDocument(request)).lines();
	}

}
