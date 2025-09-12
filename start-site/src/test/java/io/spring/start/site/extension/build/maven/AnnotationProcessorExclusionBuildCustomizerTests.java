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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AnnotationProcessorExclusionBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class AnnotationProcessorExclusionBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void annotationProcessorsAreExcludedOnlyIfTheyAreNotHandledWithMetadata() {
		ProjectRequest request = createProjectRequest("lombok", "configuration-processor");
		assertThat(mavenPom(request)).lines()
			.containsSequence("					<excludes>", "						<exclude>",
					"							<groupId>org.projectlombok</groupId>",
					"							<artifactId>lombok</artifactId>", "						</exclude>",
					"					</excludes>");

	}

	@Test
	void nonAnnotationProcessorsAreIgnored() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).lines()
			.doesNotContainSequence("						<exclude>",
					"							<groupId>org.springframework.boot</groupId>",
					"							<artifactId>spring-boot-starter-web</artifactId>",
					"						</exclude>");

	}

}
