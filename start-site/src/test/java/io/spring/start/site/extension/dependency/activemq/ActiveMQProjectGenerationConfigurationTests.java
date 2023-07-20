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

package io.spring.start.site.extension.dependency.activemq;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ActiveMQProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 */
class ActiveMQProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void activeMQWithSpringBoot30RemovesDependency() {
		ProjectRequest request = createProjectRequest("activemq");
		request.setBootVersion("3.0.0");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot", "spring-boot-starter-activemq");
	}

	@Test
	void activeMQWithSpringBoot31KeepsDependency() {
		ProjectRequest request = createProjectRequest("activemq");
		request.setBootVersion("3.1.0");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-activemq");
	}

	@Test
	void activeMQWithSpringBoot30AddsWarning() {
		ProjectRequest request = createProjectRequest("activemq");
		request.setBootVersion("3.0.0");
		assertHelpDocument(request).contains("ActiveMQ is not supported with Spring Boot 3.0");
	}

	@Test
	void activeMQWithSpringBoot31DoesNotAddWarning() {
		ProjectRequest request = createProjectRequest("activemq");
		request.setBootVersion("3.1.0");
		assertHelpDocument(request).doesNotContain("ActiveMQ is not supported with Spring Boot 3.0");
	}

	@Test
	void dockerComposeWhenDockerComposeIsNotSelectedDoesNotCreateService() {
		ProjectRequest request = createProjectRequest("web", "activemq");
		request.setBootVersion("3.2.0-M1");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void dockerComposeWhenIncompatibleSpringBootVersionDoesNotCreateService() {
		ProjectRequest request = createProjectRequest("docker-compose", "activemq");
		request.setBootVersion("3.1.1");
		assertThat(composeFile(request)).doesNotContain("activemq");
	}

	@Test
	void dockerComposeCreatesAppropriateService() {
		ProjectRequest request = createProjectRequest("docker-compose", "activemq");
		request.setBootVersion("3.2.0-M1");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/activemq.yaml"));
	}

	private TextAssert assertHelpDocument(ProjectRequest request) {
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md"));
	}

}
