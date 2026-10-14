/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
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
	@ValueSource(strings = { "prometheus" })
	void actuatorIsAddedWithPullBasedMetricsEntriesForBoot4(String dependency) {
		assertThat(generateProject(SupportedBootVersion.latest(), dependency)).mavenBuild()
			.hasDependency(getDependency("actuator"))
			.doesNotHaveDependency("org.springframework.boot", "spring-boot-starter-micrometer-metrics")
			.doesNotHaveDependency("org.springframework.boot", "spring-boot-starter-micrometer-metrics-test");
	}

	@ParameterizedTest
	@ValueSource(strings = { "datadog", "dynatrace", "influx", "graphite", "new-relic", "otlp-metrics" })
	void actuatorIsNotAddedWithPushBasedMetricsEntriesForBoot4(String dependency) {
		Dependency actuator = getDependency("actuator");
		assertThat(generateProject(SupportedBootVersion.latest(), dependency)).mavenBuild()
			.doesNotHaveDependency(actuator.getGroupId(), actuator.getArtifactId())
			.hasDependency("org.springframework.boot", "spring-boot-starter-micrometer-metrics")
			.hasDependency("org.springframework.boot", "spring-boot-starter-micrometer-metrics-test", null, "test");
	}

	@ParameterizedTest
	@ValueSource(strings = { "distributed-tracing", "zipkin", "opentelemetry" })
	void actuatorIsNotAddedWithTracingEntriesForBoot4(String dependency) {
		Dependency actuator = getDependency("actuator");
		assertThat(generateProject(SupportedBootVersion.latest(), dependency)).mavenBuild()
			.doesNotHaveDependency(actuator.getGroupId(), actuator.getArtifactId());
	}

	@Test
	void shouldNotAddMicrometerMetricsIfActuatorIsThere() {
		assertThat(generateProject(SupportedBootVersion.latest(), "actuator", "datadog")).mavenBuild()
			.hasDependency(getDependency("actuator"))
			.doesNotHaveDependency("org.springframework.boot", "spring-boot-starter-micrometer-metrics")
			.doesNotHaveDependency("org.springframework.boot", "spring-boot-starter-micrometer-metrics-test");
	}

	private ProjectStructure generateProject(SupportedBootVersion springBootVersion, String... dependencies) {
		ProjectRequest request = createProjectRequest(springBootVersion, dependencies);
		request.setType("maven-build");
		return generateProject(request);
	}

}
