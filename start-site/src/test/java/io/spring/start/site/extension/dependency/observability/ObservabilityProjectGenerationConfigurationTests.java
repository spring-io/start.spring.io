/*
 * Copyright 2012-2022 the original author or authors.
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

import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservabilityProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 */
class ObservabilityProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void testClassWithWavefrontDisablesMetricsExport() {
		ProjectRequest request = createProjectRequest("wavefront");
		request.setBootVersion("2.6.8");
		ProjectStructure project = generateProject(request);
		assertThat(project).asJvmModule(new JavaLanguage()).testSource("com.example.demo", "DemoApplicationTests")
				.contains("import " + TestPropertySource.class.getName())
				.contains("@TestPropertySource(properties = \"management.metrics.export.wavefront.enabled=false\")");
	}

	@Test
	void testClassWithoutWavefrontDoesNotDisableMetricsExport() {
		ProjectRequest request = createProjectRequest("datadog");
		ProjectStructure project = generateProject(request);
		assertThat(project).asJvmModule(new JavaLanguage()).testSource("com.example.demo", "DemoApplicationTests")
				.doesNotContain("import " + TestPropertySource.class.getName()).doesNotContain(
						"@TestPropertySource(properties = \"management.metrics.export.wavefront.enabled=false\")");
	}

}
