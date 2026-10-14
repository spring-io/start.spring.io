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
 * Tests for {@link SpringAiMongoDbAtlasProjectGenerationConfiguration}
 *
 * @author Eddú Meléndez
 */
class SpringAiMongoDbAtlasProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web", "spring-ai-vectordb-mongodb-atlas");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsMongoDbAtlasService() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose",
				"spring-ai-vectordb-mongodb-atlas");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/mongodb-atlas.yaml"));
	}

}
