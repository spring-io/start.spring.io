/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservabilityProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 */
class ObservabilityProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void zipkinShouldNotAddDistributedTracingIfNotNecessary() {
		assertThat(generateProject(SupportedBootVersion.latest(), "zipkin")).mavenBuild()
			.hasDependency(getDependency("zipkin"))
			.doesNotHaveDependency("org.springframework.boot", "spring-boot-micrometer-tracing-brave")
			.doesNotHaveDependency("io.micrometer", "micrometer-tracing-bridge-brave");
	}

	@Test
	void opentelemetryShouldNotAddDistributedTracing() {
		assertThat(generateProject(SupportedBootVersion.latest(), "opentelemetry", "distributed-tracing")).mavenBuild()
			.hasDependency(getDependency("opentelemetry"))
			.doesNotHaveDependency("org.springframework.boot", "spring-boot-micrometer-tracing-brave")
			.doesNotHaveDependency("io.micrometer", "micrometer-tracing-bridge-brave");
	}

	private ProjectStructure generateProject(SupportedBootVersion bootVersion, String... dependencies) {
		ProjectRequest request = createProjectRequest(bootVersion, dependencies);
		request.setType("maven-build");
		return generateProject(request);
	}

}
