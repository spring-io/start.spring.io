/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.liquibase;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LiquibaseProjectContributor}.
 *
 * @author Eddú Meléndez
 */
class LiquibaseProjectContributorTests extends AbstractExtensionTests {

	@Test
	void liquibaseMigrationDirectoryIsCreatedWithLiquibase() {
		ProjectRequest request = createProjectRequest("web", "liquibase");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/main/resources/db/changelog")).exists().isDirectory();
	}

	@Test
	void liquibaseMigrationDirectoryIsNotCreatedIfLiquibaseIsNotRequested() {
		ProjectRequest request = createProjectRequest("web");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/main/resources/db")).doesNotExist();
	}

}
