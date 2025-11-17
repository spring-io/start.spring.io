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

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservabilityActuatorBuildCustomizer}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class ObservabilityActuatorBuildCustomizerTests extends AbstractExtensionTests {

	@ParameterizedTest
	@ValueSource(strings = { "datadog", "dynatrace", "influx", "graphite", "new-relic", "otlp-metrics", "prometheus",
			"distributed-tracing", "zipkin", "wavefront" })
	void actuatorIsAddedWithObservabilityEntriesForBoot3(String dependency) {
		assertThat(generateProject(SupportedBootVersion.V3_5, dependency)).mavenBuild()
			.hasDependency(getDependency("actuator"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "distributed-tracing", "zipkin" })
	void actuatorIsAddedWithTracingEntriesForBoot4(String dependency) {
		assertThat(generateProject(SupportedBootVersion.V4_0, dependency)).mavenBuild()
			.hasDependency(getDependency("actuator"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "prometheus" })
	void actuatorIsAddedWithPullBasedMetricsEntriesForBoot4(String dependency) {
		assertThat(generateProject(SupportedBootVersion.V4_0, dependency)).mavenBuild()
			.hasDependency(getDependency("actuator"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "datadog", "dynatrace", "influx", "graphite", "new-relic", "otlp-metrics" })
	void actuatorIsNotAddedWithPushBasedMetricsEntriesForBoot4(String dependency) {
		Dependency actuator = getDependency("actuator");
		assertThat(generateProject(SupportedBootVersion.V4_0, dependency)).mavenBuild()
			.doesNotHaveDependency(actuator.getGroupId(), actuator.getArtifactId());
	}

	private ProjectStructure generateProject(SupportedBootVersion springBootVersion, String... dependencies) {
		ProjectRequest request = createProjectRequest(springBootVersion, dependencies);
		request.setType("maven-build");
		return generateProject(request);
	}

}
