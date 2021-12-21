/*
 * Copyright 2012-2021 the original author or authors.
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

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringGraphQlBuildCustomizer}.
 *
 * @author Brian Clozel
 */
class SpringGraphQlBuildCustomizerTests extends AbstractExtensionTests {

	private Dependency graphQlTest;

	private Dependency webFlux;

	@BeforeEach
	void setup() {
		this.graphQlTest = Dependency.withId("spring-graphql-test", "org.springframework.graphql",
				"spring-graphql-test");
		this.graphQlTest.setScope(Dependency.SCOPE_TEST);
		this.webFlux = Dependency.withId("spring-webflux", "org.springframework", "spring-webflux");
		this.webFlux.setScope(Dependency.SCOPE_TEST);
	}

	@Test
	void shouldAddTestingDependencyWhenWebFlux() {
		ProjectRequest request = createProjectRequest("webflux", "graphql");
		request.setBootVersion("2.7.0-SNAPSHOT");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("webflux"))
				.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
				.hasDependency(this.graphQlTest).doesNotHaveDependency("org.springframework", "spring-webflux")
				.hasDependenciesSize(5);
	}

	@Test
	void shouldAddTestingDependencyAndWebFluxWhenWeb() {
		ProjectRequest request = createProjectRequest("web", "graphql");
		request.setBootVersion("2.7.0-SNAPSHOT");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("web"))
				.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
				.hasDependency(this.graphQlTest).hasDependency(this.webFlux).hasDependenciesSize(5);
	}

}
