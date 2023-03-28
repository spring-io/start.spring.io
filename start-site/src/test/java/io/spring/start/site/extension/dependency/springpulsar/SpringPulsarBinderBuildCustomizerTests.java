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

package io.spring.start.site.extension.dependency.springpulsar;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringPulsarBinderBuildCustomizer}.
 *
 * @author Chris Bono
 */
class SpringPulsarBinderBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void binderNotAddedWhenPulsarNotSelected() {
		ProjectStructure project = generateProject(createProjectRequest("cloud-stream"));
		assertNoBinder(project);
	}

	@Test
	void binderNotAddedWhenCloudStreamNotSelected() {
		ProjectRequest request = createProjectRequest("pulsar");
		request.setBootVersion("3.0.4");
		ProjectStructure project = generateProject(request);
		assertNoBinder(project);
		assertThat(project).mavenBuild().hasDependency(getDependency("pulsar"));
	}

	@Test
	void binderAddedWhenPulsarAndCloudStreamSelected() {
		ProjectRequest request = createProjectRequest("pulsar", "cloud-stream");
		request.setBootVersion("3.0.4");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild()
			.hasDependency("org.springframework.pulsar", "spring-pulsar-spring-cloud-stream-binder", "0.2.0");
	}

	private void assertNoBinder(ProjectStructure project) {
		assertThat(project).mavenBuild()
			.doesNotHaveDependency("org.springframework.pulsar", "spring-pulsar-spring-cloud-stream-binder");
	}

}
