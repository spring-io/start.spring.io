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

package io.spring.start.site.extension.dependency.sbom;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SbomProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class SbomProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void shouldNotAddGradlePluginIfSbomIsNotSelected() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(gradleBuild(request)).doesNotContain("org.cyclonedx.bom");
	}

	@Test
	void shouldNotAddMavenPluginIfSbomIsNotSelected() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).doesNotContain("cyclonedx-maven-plugin");
	}

	@Test
	void shouldRemoveArtificalDependency() {
		ProjectRequest request = createProjectRequest("sbom-cyclone-dx");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot", "spring-boot");
		assertThat(gradleBuild(request)).doesNotContain("'org.springframework.boot:spring-boot'");
	}

	@Test
	void shouldAddMavenPlugin() {
		ProjectRequest request = createProjectRequest("sbom-cyclone-dx");
		assertThat(mavenPom(request)).lines().containsSequence(
		// @formatter:off
			"			<plugin>",
			"				<groupId>org.cyclonedx</groupId>",
			"				<artifactId>cyclonedx-maven-plugin</artifactId>",
			"			</plugin>"
        // @formatter:on
		);
	}

	@Test
	void shouldAddGradlePlugin() {
		ProjectRequest request = createProjectRequest("sbom-cyclone-dx");
		assertThat(gradleBuild(request)).hasPlugin("org.cyclonedx.bom", "2.3.0");
	}

}
