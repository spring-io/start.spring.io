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

package io.spring.start.site.extension.dependency.springbatch;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBatchTestBuildCustomizer}.
 *
 * @author Tim Riemer
 * @author Moritz Halbritter
 */
class SpringBatchTestBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldAddTestDependency() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "batch");
		assertThat(mavenPom(request)).hasDependency(springBatchTest());
	}

	@Test
	void shouldNotAddTestDependencyWithoutSpringBatch() {
		Dependency dependency = springBatchTest();
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "web");
		assertThat(mavenPom(request)).doesNotHaveDependency(dependency.getGroupId(), dependency.getArtifactId());
	}

	@Test
	void shouldNotAddTestDependencyForBoot4() {
		Dependency dependency = springBatchTest();
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "batch");
		assertThat(mavenPom(request)).doesNotHaveDependency(dependency.getGroupId(), dependency.getArtifactId());
	}

	private static Dependency springBatchTest() {
		Dependency dependency = Dependency.withId("spring-batch-test", "org.springframework.batch",
				"spring-batch-test");
		dependency.setScope(Dependency.SCOPE_TEST);
		return dependency;
	}

}
