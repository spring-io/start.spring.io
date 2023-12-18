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

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.test.project.ProjectAssetTester;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import io.spring.start.site.container.SimpleDockerServiceResolver;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SpringPulsarProjectGenerationConfiguration}.
 *
 * @author Chris Bono
 */
class SpringPulsarProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Nested
	class PulsarDependencyConfigurationTests {

		@Test
		void pulsarLegacyStarterUsedWhenBoot30orBoot31Selected() {
			ProjectRequest request = createProjectRequest("pulsar");
			request.setBootVersion("3.1.0");
			ProjectStructure project = generateProject(request);
			assertThat(project).mavenBuild()
				.hasDependency("org.springframework.pulsar", "spring-pulsar-spring-boot-starter");
		}

		@Test
		void pulsarReactiveLegacyStarterUsedWhenBoot30orBoot31Selected() {
			ProjectRequest request = createProjectRequest("pulsar-reactive");
			request.setBootVersion("3.1.0");
			ProjectStructure project = generateProject(request);
			assertThat(project).mavenBuild()
				.hasDependency("org.springframework.pulsar", "spring-pulsar-reactive-spring-boot-starter");
		}

		@Test
		void pulsarBootStarterUsedWhenBoot32Selected() {
			ProjectRequest request = createProjectRequest("pulsar");
			request.setBootVersion("3.2.0-M3");
			ProjectStructure project = generateProject(request);
			assertThat(project).mavenBuild().hasDependency("org.springframework.boot", "spring-boot-starter-pulsar");
		}

		@Test
		void pulsarReactiveBootStarterUsedWhenBoot32Selected() {
			ProjectRequest request = createProjectRequest("pulsar-reactive");
			request.setBootVersion("3.2.0-M3");
			ProjectStructure project = generateProject(request);
			assertThat(project).mavenBuild()
				.hasDependency("org.springframework.boot", "spring-boot-starter-pulsar-reactive");
		}

	}

	@Nested
	class DockerComposeConfigurationTests {

		@Test
		void serviceNotCreatedWhenDockerComposeNotSelected() {
			ProjectRequest request = createProjectRequest("pulsar");
			request.setBootVersion("3.2.0-M3");
			ProjectStructure structure = generateProject(request);
			assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
		}

		@ParameterizedTest
		@ValueSource(strings = { "3.1.3", "3.2.0-M2" })
		void serviceNotCreatedWhenIncompatibleBootVersionSelected(String bootVersion) {
			ProjectRequest request = createProjectRequest("docker-compose", "pulsar");
			request.setBootVersion(bootVersion);
			assertThat(composeFile(request)).doesNotContain("pulsar");
		}

		@ParameterizedTest
		@ValueSource(strings = { "pulsar", "pulsar-reactive" })
		void serviceCreatedWhenDockerComposeSelectedWithCompatibleBootVersion(String pulsarDependencyId) {
			ProjectRequest request = createProjectRequest("docker-compose", pulsarDependencyId);
			request.setBootVersion("3.2.0-M3");
			assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/pulsar.yaml"));
		}

	}

	@Nested
	class ServiceConnectionConfigurationTests {

		private final ProjectAssetTester projectTester = new ProjectAssetTester()
			.withConfiguration(SpringPulsarProjectGenerationConfiguration.class)
			.withBean(DockerServiceResolver.class, SimpleDockerServiceResolver::new);

		@Test
		void connectionNotAddedWhenTestcontainersNotSelected() {
			MutableProjectDescription description = new MutableProjectDescription();
			description.setPlatformVersion(Version.parse("3.2.0-M3"));
			description.addDependency("pulsar", mock(Dependency.class));
			this.projectTester.configure(description,
					(context) -> assertThat(context).doesNotHaveBean("pulsarServiceConnectionsCustomizer"));
		}

		@Test
		void connectionNotAddedWhenPulsarNotSelected() {
			MutableProjectDescription description = new MutableProjectDescription();
			description.setPlatformVersion(Version.parse("3.2.0-M3"));
			description.addDependency("testcontainers", mock(Dependency.class));
			this.projectTester.configure(description,
					(context) -> assertThat(context).doesNotHaveBean("pulsarServiceConnectionsCustomizer"));
		}

		@Test
		void connectionNotAddedWhenIncompatibleBootVersionSelected() {
			MutableProjectDescription description = new MutableProjectDescription();
			description.addDependency("pulsar", mock(Dependency.class));
			description.addDependency("testcontainers", mock(Dependency.class));
			this.projectTester.configure(description,
					(context) -> assertThat(context).doesNotHaveBean("pulsarServiceConnectionsCustomizer"));
		}

		@ParameterizedTest
		@ValueSource(strings = { "pulsar", "pulsar-reactive" })
		void connectionAddedWhenTestcontainersAndPulsarSelectedWithCompatibleBootVersion(String pulsarDependencyId) {
			MutableProjectDescription description = new MutableProjectDescription();
			description.setPlatformVersion(Version.parse("3.2.0-M3"));
			description.addDependency("testcontainers", mock(Dependency.class));
			description.addDependency(pulsarDependencyId, mock(Dependency.class));
			this.projectTester.configure(description,
					(context) -> assertThat(context)
						.getBean("pulsarServiceConnectionsCustomizer", ServiceConnectionsCustomizer.class)
						.satisfies((customizer) -> {
							ServiceConnections connections = new ServiceConnections();
							customizer.customize(connections);
							assertPulsarServiceConnectionAdded(connections);
						}));
		}

		private void assertPulsarServiceConnectionAdded(ServiceConnections connections) {
			assertThat(connections.values()).first().satisfies((connection) -> {
				assertThat(connection.id()).isEqualTo("pulsar");
				assertThat(connection.containerClassName()).isEqualTo("org.testcontainers.containers.PulsarContainer");
				assertThat(connection.isGenericContainer()).isFalse();
				assertThat(connection.containerClassNameGeneric()).isFalse();
				assertThat(connection.dockerService()).satisfies((dockerService) -> {
					assertThat(dockerService.getImage()).isEqualTo("apachepulsar/pulsar");
					assertThat(dockerService.getImageTag()).isEqualTo("latest");
					assertThat(dockerService.getWebsite()).isEqualTo("https://hub.docker.com/r/apachepulsar/pulsar");
					assertThat(dockerService.getCommand()).isEqualTo("bin/pulsar standalone");
					assertThat(dockerService.getPorts()).containsExactlyInAnyOrder(8080, 6650);
				});
			});
		}

	}

	@Nested
	class SpringPulsarBinderConfigurationTests {

		@Test
		void binderNotAddedWhenCloudStreamNotSelected() {
			ProjectRequest request = createProjectRequest("pulsar");
			request.setBootVersion("3.1.3");
			ProjectStructure project = generateProject(request);
			assertNoBinder(project);
			assertThat(project).mavenBuild()
				.hasDependency("org.springframework.pulsar", "spring-pulsar-spring-boot-starter");
		}

		@Test
		void binderNotAddedWhenPulsarNotSelected() {
			ProjectRequest request = createProjectRequest("cloud-stream");
			request.setBootVersion("3.1.3");
			ProjectStructure project = generateProject(request);
			assertNoBinder(project);
		}

		@ParameterizedTest
		@ValueSource(strings = { "3.2.0-M1", "3.2.0-M3" })
		void binderNotAddedWhenPulsarAndCloudStreamSelectedWithIncompatibleBootVersion(String bootVersion) {
			ProjectRequest request = createProjectRequest("pulsar", "cloud-stream");
			request.setBootVersion(bootVersion);
			ProjectStructure project = generateProject(request);
			assertNoBinder(project);
		}

		@Test
		void binderAddedWhenPulsarAndCloudStreamSelectedWithCompatibleBootVersion() {
			ProjectRequest request = createProjectRequest("pulsar", "cloud-stream");
			request.setBootVersion("3.1.0");
			ProjectStructure project = generateProject(request);
			assertBinder(project);
		}

		@Test
		void binderAddedWhenPulsarReactiveAndCloudStreamSelectedWithCompatibleBootVersion() {
			ProjectRequest request = createProjectRequest("pulsar-reactive", "cloud-stream");
			request.setBootVersion("3.1.0");
			ProjectStructure project = generateProject(request);
			assertBinder(project);
		}

		private void assertNoBinder(ProjectStructure project) {
			assertThat(project).mavenBuild()
				.doesNotHaveDependency("org.springframework.pulsar", "spring-pulsar-spring-cloud-stream-binder");
		}

		private void assertBinder(ProjectStructure project) {
			assertThat(project).mavenBuild()
				.hasDependency("org.springframework.pulsar", "spring-pulsar-spring-cloud-stream-binder", "0.2.0");
		}

	}

}
