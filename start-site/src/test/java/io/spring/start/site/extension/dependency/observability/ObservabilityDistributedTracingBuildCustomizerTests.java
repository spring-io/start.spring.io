/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservabilityDistributedTracingBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class ObservabilityDistributedTracingBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldAddMicrometerTracing() {
		ProjectRequest request = createProjectRequest("distributed-tracing");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).hasDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-micrometer-tracing-test",
				null, Dependency.SCOPE_TEST);
	}

	@Test
	void shouldNotAddMicrometerTracingIfZipkinIsPresent() {
		ProjectRequest request = createProjectRequest("zipkin");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-zipkin");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-zipkin-test", null,
				Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-test");
	}

	@Test
	void shouldNotAddMicrometerTracingIfZipkinAndDistributedTracingArePresent() {
		ProjectRequest request = createProjectRequest("zipkin", "distributed-tracing");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-zipkin");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-zipkin-test", null,
				Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-test");
	}

	@Test
	void shouldNotAddMicrometerTracingIfOpentelemetryIsPresent() {
		ProjectRequest request = createProjectRequest("opentelemetry");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-opentelemetry");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot",
				"spring-boot-starter-opentelemetry-test", null, Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-test");
	}

	@Test
	void shouldNotAddMicrometerTracingIfOpentelemetryAndDistributedTracingArePresent() {
		ProjectRequest request = createProjectRequest("opentelemetry", "distributed-tracing");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-opentelemetry");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot",
				"spring-boot-starter-opentelemetry-test", null, Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-test");
	}

}
