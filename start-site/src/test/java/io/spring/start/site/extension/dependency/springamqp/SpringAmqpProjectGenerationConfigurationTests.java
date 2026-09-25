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

package io.spring.start.site.extension.dependency.springamqp;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAmqpProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class SpringAmqpProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void amqpStarterIsAddedPriorToSpringBoot42() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_1, "rabbitmq");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-amqp");
	}

	@Test
	void rabbitMqStarterIsAddedAsOfSpringBoot42() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_2, "rabbitmq");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-rabbitmq");
	}

	@Test
	void springRabbitMqWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest("web", "rabbitmq");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void springRabbitMqWithDockerCompose() {
		ProjectRequest request = createProjectRequest("docker-compose", "rabbitmq");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/rabbitmq.yaml"));
	}

}
