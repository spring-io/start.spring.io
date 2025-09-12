/*
 * Copyright 2012 - present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
