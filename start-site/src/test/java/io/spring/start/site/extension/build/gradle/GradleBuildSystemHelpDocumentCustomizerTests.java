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

package io.spring.start.site.extension.build.gradle;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GradleBuildSystemHelpDocumentCustomizer}.
 *
 * @author Jenn Strater
 * @author Andy Wilkinson
 * @author Moritz Halbritter
 */
class GradleBuildSystemHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void linksAddedToHelpDocumentForGradleBuild() {
		assertHelpDocument("gradle-build").contains("* [Official Gradle documentation](https://docs.gradle.org)",
				"* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)",
				"* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.0/gradle-plugin)",
				"* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.0/gradle-plugin/packaging-oci-image.html)");
	}

	@Test
	void linksNotAddedToHelpDocumentForMavenBuild() {
		assertHelpDocument("maven-build").noneMatch((line) -> line.contains("Gradle"));
	}

	private ListAssert<String> assertHelpDocument(String type) {
		ProjectRequest request = createProjectRequest("web");
		request.setType(type);
		return assertThat(helpDocument(request)).lines();
	}

}
