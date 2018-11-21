/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.WebProjectRequest;
import org.junit.Test;

/**
 * Tests for {@link SpringBatchTestBuildCustomizer}.
 *
 * @author Tim Riemer
 */
public class SpringBatchTestBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	public void batchTestIsAddedWithBatch() {
		WebProjectRequest request = createProjectRequest("batch");
		generateMavenPom(request).hasSpringBootStarterDependency("batch")
				.hasSpringBootStarterTest().hasDependency(springBatchTest())
				.hasDependenciesCount(3);
	}

	@Test
	public void batchTestIsNotAddedWithoutSpringBatch() {
		WebProjectRequest request = createProjectRequest("web");
		generateMavenPom(request).hasSpringBootStarterDependency("web")
				.hasSpringBootStarterTest().hasDependenciesCount(2);
	}

	private static Dependency springBatchTest() {
		Dependency dependency = Dependency.withId("spring-batch-test",
				"org.springframework.batch", "spring-batch-test");
		dependency.setScope(Dependency.SCOPE_TEST);
		return dependency;
	}

}
