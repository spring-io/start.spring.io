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

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAzureDefaultBuildCustomizer}.
 *
 * @author Yonghui Ye
 */
class SpringAzureDefaultBuildCustomizerTests extends AbstractExtensionTests {

	static final Dependency AZURE_ADAPTER = Dependency.withId("spring-cloud-azure-starter", "com.azure.spring",
			"spring-cloud-azure-starter");

	@Test
	void testIsAddedWithAzureSupport() {
		ProjectRequest request = createProjectRequest("azure-support");
		assertThat(mavenPom(request)).hasDependency(getDependency("azure-support")).hasDependency(AZURE_ADAPTER)
				.hasDependenciesSize(2)
				.hasBom("com.azure.spring", "spring-cloud-azure-dependencies", "${spring-cloud-azure.version}");
	}

}
