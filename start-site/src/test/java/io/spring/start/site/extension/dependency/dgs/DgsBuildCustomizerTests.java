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

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DgsBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	private Dependency dgsTest;

	@BeforeEach
	void setup() {
		this.dgsTest = Dependency.withId("graphql-dgs-spring-graphql-starter-test", "com.netflix.graphql.dgs",
				"graphql-dgs-spring-graphql-starter-test");
		this.dgsTest.setScope(Dependency.SCOPE_TEST);
	}

	@Test
	void shouldAddTestingDependency() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web", "netflix-dgs");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("web"))
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependency(this.dgsTest)
			.hasDependenciesSize(4);
	}

}
