/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.activemq;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ArtemisProjectGenerationConfiguration}.
 *
 * @author Eddú Meléndez
 */
class ArtemisProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void dockerComposeWhenDockerComposeIsNotSelectedDoesNotCreateService() {
		ProjectRequest request = createProjectRequest("web", "artemis");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void dockerComposeCreatesApacheArtemis() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.latest(), "docker-compose", "artemis");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/apache-artemis.yaml"));
	}

}
