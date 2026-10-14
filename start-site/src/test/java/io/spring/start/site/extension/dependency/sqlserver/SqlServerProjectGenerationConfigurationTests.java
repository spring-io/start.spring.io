/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.sqlserver;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SqlServerProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class SqlServerProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest("web", "sqlserver");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsSqlServerService() {
		ProjectRequest request = createProjectRequest("docker-compose", "sqlserver");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/sqlserver.yaml"));
	}

}
