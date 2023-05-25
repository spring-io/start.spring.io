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

package io.spring.start.site.extension.dependency.cassandra;

import java.nio.charset.StandardCharsets;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CassandraDockerComposeProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class CassandraDockerComposeProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest("web", "data-cassandra", "data-cassandra-reactive");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsCassandraService() {
		ProjectRequest request = createProjectRequest("docker-compose", "data-cassandra");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).exists()
			.content(StandardCharsets.UTF_8)
			.containsIgnoringNewLines(cassandraService());
	}

	@Test
	void createsCassandraServiceWhenReactive() {
		ProjectRequest request = createProjectRequest("docker-compose", "data-cassandra-reactive");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).exists()
			.content(StandardCharsets.UTF_8)
			.containsIgnoringNewLines(cassandraService());
	}

	@Test
	void doesNotFailWhenBothCassandraAndReactiveCassandraAreSelected() {
		ProjectRequest request = createProjectRequest("docker-compose", "data-cassandra", "data-cassandra-reactive");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).exists()
			.content(StandardCharsets.UTF_8)
			.containsIgnoringNewLines(cassandraService());
	}

	private static String cassandraService() {
		return """
				services:
				  cassandra:
				    image: 'cassandra:latest'
				    environment:
				      - 'CASSANDRA_DC=dc1'
				      - 'CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch'
				    ports:
				      - '9042'""";
	}

}
