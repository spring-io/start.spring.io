/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.flyway;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FlywayProjectContributor}.
 *
 * @author Stephane Nicoll
 */
class FlywayProjectContributorTests extends AbstractExtensionTests {

	@Test
	void flywayMigrationDirectoryIsCreatedWithFlyway() {
		ProjectRequest request = createProjectRequest("web", "flyway");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/main/resources/db/migration")).exists().isDirectory();
	}

	@Test
	void flywayMigrationDirectoryIsNotCreatedIfFlywayIsNotRequested() {
		ProjectRequest request = createProjectRequest("web");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/main/resources/db")).doesNotExist();
	}

}
