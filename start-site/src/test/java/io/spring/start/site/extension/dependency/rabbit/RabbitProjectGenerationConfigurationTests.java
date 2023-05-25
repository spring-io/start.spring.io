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

package io.spring.start.site.extension.dependency.rabbit;

import java.nio.charset.StandardCharsets;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RabbitProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class RabbitProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest("web", "amqp");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsRabbitService() {
		ProjectRequest request = createProjectRequest("docker-compose", "amqp");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).exists()
			.content(StandardCharsets.UTF_8)
			.containsIgnoringNewLines(rabbitService());
	}

	private static String rabbitService() {
		return """
				services:
				  rabbitmq:
				    image: 'rabbitmq:latest'
				    environment:
				      - 'RABBITMQ_DEFAULT_PASS=secret'
				      - 'RABBITMQ_DEFAULT_USER=myuser'
				    ports:
				      - '5672'""";
	}

}
