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

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAzureMavenBuildCustomizer}.
 *
 * @author Muyao Feng
 * @author Moritz Halbritter
 */
class SpringAzureMavenBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V3_4;

	@Test
	void shouldDoNothingIfAzureSupportIsntSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web");
		assertThat(mavenPom(request)).doesNotContain("azure-container-apps-maven-plugin");
	}

	@Test
	void azureContainerAppsMavenPluginAddedWhenAzureSupportPresent() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "azure-support");
		assertThat(mavenPom(request)).hasText("/project/build/plugins/plugin[1]/groupId", "com.microsoft.azure")
			.hasText("/project/build/plugins/plugin[1]/artifactId", "azure-container-apps-maven-plugin");
	}

}
