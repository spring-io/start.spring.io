/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.mariadb;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MariaDbProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 * @author Eddú Meléndez
 */
class MariaDbProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest("web", "mariadb");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsMariaDbService() {
		ProjectRequest request = createProjectRequest("docker-compose", "mariadb");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/mariadb.yaml"));
	}

	@Test
	void createsMariaDbServiceSpringAi() {
		ProjectRequest request = createProjectRequest("docker-compose", "spring-ai-vectordb-mariadb");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/mariadb.yaml"));
	}

}
