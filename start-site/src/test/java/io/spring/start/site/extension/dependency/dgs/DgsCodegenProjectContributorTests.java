/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DgsCodegenProjectContributor}.
 *
 * @author Brian Clozel
 */
class DgsCodegenProjectContributorTests extends AbstractExtensionTests {

	@Test
	void graphqlClientResourceDirectoryIsCreatedWithDgsCodegen() {
		ProjectRequest request = createProjectRequest("dgs-codegen");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/main/resources/graphql-client")).exists().isDirectory();
	}

	@Test
	void graphqlClientResourceDirectoryIsNotCreatedIfDgsCodegenIsNotRequested() {
		ProjectRequest request = createProjectRequest("web");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("src/main/resources/graphql-client")).doesNotExist();
	}

}
