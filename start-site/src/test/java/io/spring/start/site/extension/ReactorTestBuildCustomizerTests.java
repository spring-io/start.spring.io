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
 * Tests for {@link ReactorTestBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class ReactorTestBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void reactorTestIsAdded() {
		ProjectRequest request = createProjectRequest("webflux");
		request.setBootVersion("2.0.0.M2");
		Dependency reactorTest = Dependency.withId("reactor-test", "io.projectreactor", "reactor-test");
		reactorTest.setScope(Dependency.SCOPE_TEST);
		generateMavenPom(request).hasSpringBootStarterDependency("webflux").hasSpringBootStarterTest()
				.hasDependency(reactorTest).hasDependenciesCount(3);
	}

	@Test
	void reactorTestIsNotAddedWithEarlierVersions() {
		ProjectRequest request = createProjectRequest("webflux");
		request.setBootVersion("2.0.0.M1");
		generateMavenPom(request).hasSpringBootStarterDependency("webflux").hasSpringBootStarterTest()
				.hasDependenciesCount(2);
	}

	@Test
	void reactorTestIsNotAddedWithoutWebFlux() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.0.M2");
		generateMavenPom(request).hasSpringBootStarterDependency("web").hasSpringBootStarterTest()
				.hasDependenciesCount(2);
	}

}
