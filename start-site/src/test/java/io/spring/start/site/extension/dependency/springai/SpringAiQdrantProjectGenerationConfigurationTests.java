/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAiQdrantProjectGenerationConfiguration}
 *
 * @author Eddú Meléndez
 */
class SpringAiQdrantProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web", "spring-ai-vectordb-qdrant");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsQdrantService() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "spring-ai-vectordb-qdrant");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/qdrant.yaml"));
	}

}
