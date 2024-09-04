/*
 * Copyright 2012-2024 the original author or authors.
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

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservabilityHelpDocumentCustomizer}.
 *
 * @author Moritz Halbritter
 */
class ObservabilityHelpDocumentCustomizerTests extends AbstractExtensionTests {

	private static final String OLD_SPRING_BOOT_VERSION = "3.2.0";

	private static final String SPRING_BOOT_VERSION = "3.3.0";

	@Test
	void linksAddedToHelpDocumentForGradleBuild() {
		assertHelpDocument(SPRING_BOOT_VERSION, "distributed-tracing").contains(
				"* [Distributed Tracing Reference Guide](https://docs.micrometer.io/tracing/reference/index.html)",
				"* [Getting Started with Distributed Tracing](https://docs.spring.io/spring-boot/3.3.0/reference/actuator/tracing.html)");
	}

	@Test
	void linksAddedToHelpDocumentForGradleBuildWithOldSpringBootVersion() {
		assertHelpDocument(OLD_SPRING_BOOT_VERSION, "distributed-tracing").contains(
				"* [Distributed Tracing Reference Guide](https://docs.micrometer.io/tracing/reference/index.html)",
				"* [Getting Started with Distributed Tracing](https://docs.spring.io/spring-boot/docs/3.2.0/reference/html/actuator.html#actuator.micrometer-tracing.getting-started)");
	}

	@Test
	void linksNotAddedToHelpDocumentForBuildWithoutTracing() {
		assertHelpDocument(SPRING_BOOT_VERSION).noneMatch((line) -> line.contains("Tracing"));
	}

	private ListAssert<String> assertHelpDocument(String version, String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setType("gradle-build");
		request.setBootVersion(version);
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md")).lines();
	}

}
