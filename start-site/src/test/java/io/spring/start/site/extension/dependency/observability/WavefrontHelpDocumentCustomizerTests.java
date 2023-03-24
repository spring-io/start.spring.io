/*
 * Copyright 2012-2023 the original author or authors.
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
 * Tests for {@link WavefrontHelpDocumentCustomizer}.
 *
 * @author Stephane Nicoll
 * @author Brian Clozel
 */
class WavefrontHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void wavefrontAddGeneralSection() {
		assertHelpDocument("2.7.5", "wavefront").contains("## Observability with Wavefront", "",
				"If you don't have a Wavefront account, the starter will create a freemium account for you.",
				"The URL to access the Wavefront Service dashboard is logged on startup.");
	}

	@Test
	void wavefrontWithoutWebApplicationDoesNotAddActuatorSection() {
		assertHelpDocument("2.7.5", "wavefront")
			.doesNotContain("You can also access your dashboard using the `/actuator/wavefront` endpoint.");
	}

	@Test
	void wavefrontWithWebApplicationAddActuatorSection() {
		assertHelpDocument("2.7.5", "wavefront", "web")
			.contains("You can also access your dashboard using the `/actuator/wavefront` endpoint.");
	}

	@Test
	void wavefrontWithoutSleuthAddTracingNote() {
		assertHelpDocument("2.7.5", "wavefront")
			.contains("Finally, you can opt-in for distributed tracing by adding the 'Distributed Tracing' entry.");
	}

	@Test
	void wavefrontWithSleuthDoesNotAddTracingNote() {
		assertHelpDocument("2.7.5", "wavefront", "distributed-tracing").doesNotContain(
				"Finally, you can opt-in for distributed tracing by adding the 'Distributed Tracing' entry.");
	}

	@Test
	void springBoot2xWavefrontReference() {
		assertHelpDocument("2.7.5", "wavefront").contains(
				"* [Wavefront for Spring Boot documentation](https://docs.wavefront.com/wavefront_springboot.html)");
	}

	@Test
	void springBoot3xWavefrontReference() {
		assertHelpDocument("3.0.0", "wavefront").contains(
				"* [Wavefront for Spring Boot documentation](https://docs.wavefront.com/wavefront_springboot3.html)");
	}

	private ListAssert<String> assertHelpDocument(String version, String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setBootVersion(version);
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md")).lines();
	}

}
