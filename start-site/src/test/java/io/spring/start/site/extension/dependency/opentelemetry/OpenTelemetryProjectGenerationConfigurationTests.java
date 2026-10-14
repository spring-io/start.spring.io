/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.opentelemetry;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OpenTelemetryProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class OpenTelemetryProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest("web", "opentelemetry");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsDockerComposeService() {
		ProjectRequest request = createProjectRequest("docker-compose", "opentelemetry");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/opentelemetry.yaml"));
	}

	@Test
	void doesNothingWithoutTestcontainers() {
		ProjectRequest request = createProjectRequest("web", "opentelemetry");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory()
			.resolve("src/test/java/com/example/demo/TestcontainersConfiguration.java")).doesNotExist();
	}

	@Test
	void createsTestcontainersBean() {
		ProjectRequest request = createProjectRequest("testcontainers", "opentelemetry");
		request.setLanguage("java");
		ProjectStructure projectStructure = generateProject(request);
		assertThat(projectStructure).textFile("src/test/java/com/example/demo/TestcontainersConfiguration.java")
			.isEqualToNormalizingNewlines("""
					package com.example.demo;

					import org.springframework.boot.test.context.TestConfiguration;
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
					import org.springframework.context.annotation.Bean;
					import org.testcontainers.grafana.LgtmStackContainer;
					import org.testcontainers.utility.DockerImageName;

					@TestConfiguration(proxyBeanMethods = false)
					class TestcontainersConfiguration {

						@Bean
						@ServiceConnection
						LgtmStackContainer grafanaLgtmContainer() {
							return new LgtmStackContainer(DockerImageName.parse("grafana/otel-lgtm:latest"));
						}

					}
					""");
	}

}
