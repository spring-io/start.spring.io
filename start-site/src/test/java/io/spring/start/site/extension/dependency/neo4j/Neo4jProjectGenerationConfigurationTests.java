/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.neo4j;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Neo4jProjectGenerationConfiguration}.
 *
 * @author Eddú Meléndez
 */
class Neo4jProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web", "data-neo4j");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsNeo4jService() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "data-neo4j");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/neo4j.yaml"));
	}

	@Test
	void createsNeo4jServiceWhenDriverIsSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "neo4j");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/neo4j.yaml"));
	}

	@Test
	void createsNeo4jServiceWhenSpringAiModuleIsSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "spring-ai-vectordb-neo4j");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/neo4j.yaml"));
	}

	@Test
	void createsNeo4jServiceWhenSpringAiChatMemoryModuleIsSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose",
				"spring-ai-chat-memory-repository-neo4j");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/neo4j.yaml"));
	}

}
