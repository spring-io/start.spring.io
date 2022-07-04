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

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAzureActuatorBuildCustomizer}.
 *
 * @author Yonghui Ye
 */
class SpringAzureActuatorBuildCustomizerTests extends AbstractExtensionTests {

	static final Dependency ACTUATOR_ADAPTER = Dependency.withId("spring-cloud-azure-starter-actuator",
			"com.azure.spring", "spring-cloud-azure-starter-actuator");

	@Test
	void actuatorTestIsAddedWithActuator() {
		ProjectRequest request = createProjectRequest("actuator", "azure-cosmos-db");
		assertThat(mavenPom(request)).hasDependency(getDependency("azure-cosmos-db")).hasDependency(ACTUATOR_ADAPTER)
				.hasDependenciesSize(4)
				.hasBom("com.azure.spring", "spring-cloud-azure-dependencies", "${spring-cloud-azure.version}");
	}

}
