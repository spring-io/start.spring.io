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

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservabilityActuatorBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class ObservabilityActuatorBuildCustomizerTests extends AbstractExtensionTests {

	@ParameterizedTest
	@ValueSource(strings = { "datadog", "dynatrace", "influx", "graphite", "new-relic", "distributed-tracing", "zipkin",
			"wavefront" })
	void actuatorIsAddedWithObservabilityEntries(String dependency) {
		assertThat(generateProject("3.1.0", dependency)).mavenBuild().hasDependency(getDependency("actuator"));
	}

	private ProjectStructure generateProject(String bootVersion, String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setBootVersion(bootVersion);
		request.setType("maven-build");
		return generateProject(request);
	}

}
