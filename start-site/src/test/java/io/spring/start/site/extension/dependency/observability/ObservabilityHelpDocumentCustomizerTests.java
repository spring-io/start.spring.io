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

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservabilityHelpDocumentCustomizer}.
 *
 * @author Moritz Halbritter
 */
class ObservabilityHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void linksAddedToHelpDocumentForGradleBuild() {
		assertHelpDocument("distributed-tracing").contains(
				"* [Distributed Tracing Reference Guide](https://docs.micrometer.io/tracing/reference/index.html)",
				"* [Getting Started with Distributed Tracing](https://docs.spring.io/spring-boot/3.5.0/reference/actuator/tracing.html)");
	}

	@Test
	void linksNotAddedToHelpDocumentForBuildWithoutTracing() {
		assertHelpDocument().noneMatch((line) -> line.contains("Tracing"));
	}

	private ListAssert<String> assertHelpDocument(String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setType("gradle-build");
		return assertThat(helpDocument(request)).lines();
	}

}
