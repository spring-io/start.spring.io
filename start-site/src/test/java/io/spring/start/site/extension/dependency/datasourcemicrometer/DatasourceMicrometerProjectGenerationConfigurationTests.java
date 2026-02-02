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

package io.spring.start.site.extension.dependency.datasourcemicrometer;

import io.spring.initializr.generator.test.buildsystem.maven.MavenBuildAssert;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.AssertProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DatasourceMicrometerProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class DatasourceMicrometerProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void shouldNotAddOtelModuleByDefault() {
		AssertProvider<MavenBuildAssert> pom = mavenPom(createProjectRequest("datasource-micrometer"));
		assertThat(pom).doesNotHaveDependency("net.ttddyy.observation", "datasource-micrometer-opentelemetry");
	}

	@Test
	void shouldAddOtelModuleIfOpenTelemetryIsSelected() {
		AssertProvider<MavenBuildAssert> pom = mavenPom(createProjectRequest("datasource-micrometer", "opentelemetry"));
		assertThat(pom).hasDependency("net.ttddyy.observation", "datasource-micrometer-opentelemetry");
	}

}
