/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graphql;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringGraphQlProjectContributor}.
 *
 * @author Andy Wilkinson
 */
class SpringGraphQlProjectContributorTests extends AbstractExtensionTests {

	@Test
	void graphqlResourceDirectoryIsCreatedWithSpringGraphQl() {
		ProjectRequest request = createProjectRequest("graphql");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/main/resources/graphql")).exists().isDirectory();
	}

	@Test
	void graphqlResourceDirectoryIsNotCreatedIfSpringGraphQlIsNotRequested() {
		ProjectRequest request = createProjectRequest("web");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/main/resources/graphql")).doesNotExist();
	}

}
