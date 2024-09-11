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
 * Tests for {@link SpringAzureDockerComposeProjectGenerationConfiguration}.
 *
 * @author Eddú Meléndez
 */
class SpringAzureDockerComposeProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void springAzureDockerComposeDependencyIsAdded() {
		ProjectRequest projectRequest = createProjectRequest("docker-compose", "azure-storage");
		assertThat(mavenPom(projectRequest)).hasDependency("com.azure.spring", "spring-cloud-azure-docker-compose",
				null, "test");
	}

	@Test
	void shouldNotAddDockerComposeTestcontainersDependencyIfNoSpringAzureDependencyIsSelected() {
		ProjectRequest projectRequest = createProjectRequest("docker-compose", "web");
		assertThat(mavenPom(projectRequest)).doesNotHaveDependency("com.azure.spring",
				"spring-cloud-azure-docker-compose");
	}

}
