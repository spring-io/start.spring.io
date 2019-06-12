/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JacksonKotlinBuildCustomizer}.
 *
 * @author Sebastien Deleuze
 * @author Stephane Nicoll
 */
class JacksonKotlinBuildCustomizerTests extends AbstractExtensionTests {

	private static final Dependency JACKSON_KOTLIN = Dependency.withId("jackson-module-kotlin",
			"com.fasterxml.jackson.module", "jackson-module-kotlin");

	private static final Dependency REACTOR_TEST = Dependency.withId("reactor-test", "io.projectreactor",
			"reactor-test", null, Dependency.SCOPE_TEST);

	@Test
	void jacksonModuleKotlinIsAdded() {
		ProjectRequest request = createProjectRequest("webflux");
		request.setBootVersion("2.0.0.M2");
		request.setLanguage("kotlin");
		generateMavenPom(request).hasSpringBootStarterDependency("webflux").hasDependency(JACKSON_KOTLIN)
				.hasSpringBootStarterTest().hasDependency(REACTOR_TEST)
				.hasDependency("org.jetbrains.kotlin", "kotlin-reflect")
				.hasDependency("org.jetbrains.kotlin", "kotlin-stdlib-jdk8").hasDependenciesCount(6);
	}

	@Test
	void jacksonModuleKotlinIsNotAddedWithoutKotlin() {
		ProjectRequest request = createProjectRequest("webflux");
		request.setBootVersion("2.0.0.M2");
		generateMavenPom(request).hasSpringBootStarterDependency("webflux").hasSpringBootStarterTest()
				.hasDependency(REACTOR_TEST).hasDependenciesCount(3);
	}

	@Test
	void jacksonModuleKotlinIsNotAddedWithoutJsonFacet() {
		ProjectRequest request = createProjectRequest("actuator");
		request.setBootVersion("2.0.0.M2");
		request.setLanguage("kotlin");
		generateMavenPom(request).hasSpringBootStarterDependency("actuator").hasSpringBootStarterTest()
				.hasDependency("org.jetbrains.kotlin", "kotlin-reflect")
				.hasDependency("org.jetbrains.kotlin", "kotlin-stdlib-jdk8").hasDependenciesCount(4);
	}

}
