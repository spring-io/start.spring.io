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

package io.spring.start.site.extension.dependency.opentelemetry;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
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
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "web", "opentelemetry");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsDockerComposeService() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "docker-compose", "opentelemetry");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/opentelemetry.yaml"));
	}

	@Test
	void doesNothingWithoutTestcontainers() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "web", "opentelemetry");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory()
			.resolve("src/test/java/com/example/demo/TestcontainersConfiguration.java")).doesNotExist();
	}

	@Test
	void createsTestcontainersBean() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "testcontainers", "opentelemetry");
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
