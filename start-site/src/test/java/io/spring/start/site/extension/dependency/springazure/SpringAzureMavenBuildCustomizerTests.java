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

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAzureMavenBuildCustomizer}.
 *
 * @author Muyao Feng
 */
class SpringAzureMavenBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void azureContainerAppsMavenPluginAddedWhenAzureSupportPresent() {
		ProjectRequest request = createProjectRequest("azure-support");
		assertThat(mavenPom(request)).hasText("/project/build/plugins/plugin[1]/groupId", "com.microsoft.azure");
		assertThat(mavenPom(request)).hasText("/project/build/plugins/plugin[1]/artifactId",
				"azure-container-apps-maven-plugin");
	}

}
