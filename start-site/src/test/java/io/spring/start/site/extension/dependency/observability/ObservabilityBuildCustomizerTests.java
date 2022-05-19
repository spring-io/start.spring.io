/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservabilityBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class ObservabilityBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void actuatorIsAddedWithDataDog() {
		assertThat(generateProject("datadog")).mavenBuild().hasDependency(getDependency("actuator"));
	}

	@Test
	void actuatorIsAddedWithInflux() {
		assertThat(generateProject("influx")).mavenBuild().hasDependency(getDependency("actuator"));
	}

	@Test
	void actuatorIsAddedWithGraphite() {
		assertThat(generateProject("graphite")).mavenBuild().hasDependency(getDependency("actuator"));
	}

	@Test
	void actuatorIsAddedWithNewRelic() {
		assertThat(generateProject("new-relic")).mavenBuild().hasDependency(getDependency("actuator"));
	}

	@Test
	void actuatorIsNotAddedWithWavefrontStarter() {
		Dependency actuator = getDependency("actuator");
		assertThat(generateProject("wavefront")).mavenBuild().doesNotHaveDependency(actuator.getGroupId(),
				actuator.getArtifactId());
	}

	private ProjectStructure generateProject(String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setBootVersion("2.6.8");
		request.setType("maven-build");
		return generateProject(request);
	}

}
