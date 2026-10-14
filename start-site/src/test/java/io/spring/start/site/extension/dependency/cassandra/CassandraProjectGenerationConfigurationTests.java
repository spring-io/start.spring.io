/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.cassandra;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CassandraProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 * @author Eddú Meléndez
 */
class CassandraProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web", "data-cassandra", "data-cassandra-reactive");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsCassandraService() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "data-cassandra");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/cassandra.yaml"));
	}

	@Test
	void createsCassandraServiceWhenReactive() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "data-cassandra-reactive");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/cassandra.yaml"));
	}

	@Test
	void createsCassandraServiceWhenSpringAiIsSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "spring-ai-vectordb-cassandra");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/cassandra.yaml"));
	}

	@Test
	void doesNotFailWhenBothCassandraAndReactiveCassandraAreSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "data-cassandra",
				"data-cassandra-reactive");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/cassandra.yaml"));
	}

	@Test
	void createsCassandraServiceWhenDriverIsSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "cassandra");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/cassandra.yaml"));
	}

	@Test
	void createsCassandraServiceWhenSpringAiChatMemoryIsSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose",
				"spring-ai-chat-memory-repository-cassandra");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/cassandra.yaml"));
	}

}
