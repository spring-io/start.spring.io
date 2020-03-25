/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.start.site.extension.dependency.solace;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SolaceBinderBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class SolaceBinderBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void binderNotAddedWhenSolaceNotSelected() {
		ProjectStructure project = generateProject(createProjectRequest("cloud-stream"));
		assertNoBinder(project);
	}

	@Test
	void binderNotAddedWhenCloudStreamNotSelected() {
		ProjectStructure project = generateProject(createProjectRequest("solace"));
		assertNoBinder(project);
		assertThat(project).mavenBuild().hasDependency(getDependency("solace"));
	}

	@Test
	void binderAddedWhenSolaceAndCloudStreamSelected() {
		ProjectStructure project = generateProject(createProjectRequest("solace", "cloud-stream"));
		assertThat(project).mavenBuild().hasDependency("com.solace.spring.cloud", "spring-cloud-starter-stream-solace");
	}

	@Test
	void bomAddedWhenSolaceAndCloudStreamSelected() {
		ProjectStructure project = generateProject(createProjectRequest("solace", "cloud-stream"));
		assertThat(project).mavenBuild().hasBom("com.solace.spring.cloud", "solace-spring-cloud-bom",
				"${solace-spring-cloud.version}");
	}

	@Test
	void bomPropertyAddedWhenSolaceAndCloudStreamSelected() {
		String platformVersion = "2.2.6.RELEASE";
		ProjectRequest request = createProjectRequest("solace", "cloud-stream");
		request.setBootVersion(platformVersion);
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild().hasProperty("solace-spring-cloud.version",
				getBom("solace-spring-cloud", platformVersion).getVersion());
	}

	@Test
	void solaceStarterRemovedWhenSolaceAndCloudStreamSelected() {
		ProjectStructure project = generateProject(createProjectRequest("solace", "cloud-stream"));
		Dependency solace = getDependency("solace");
		assertThat(project).mavenBuild().doesNotHaveDependency(solace.getGroupId(), solace.getArtifactId());
	}

	private void assertNoBinder(ProjectStructure project) {
		assertThat(project).mavenBuild()
				.doesNotHaveDependency("com.solace.spring.cloud", "spring-cloud-starter-stream-solace")
				.doesNotHaveBom("com.solace.spring.cloud", "solace-spring-cloud-bom")
				.doesNotHaveProperty("solace-spring-cloud.version");
	}

}
