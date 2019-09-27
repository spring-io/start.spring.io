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

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringCloudContractMavenBuildCustomizer}.
 *
 * @author Olga Maciaszek-Sharma
 * @author Madhura Bhave
 * @author Eddú Meléndez
 */
class SpringCloudContractMavenBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void springCloudContractVerifierPluginAddedWhenSCCDependencyPresent() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("cloud-contract-verifier"))
				.hasText("/project/build/plugins/plugin[1]/groupId", "org.springframework.cloud")
				.hasText("/project/build/plugins/plugin[1]/artifactId", "spring-cloud-contract-maven-plugin")
				.hasText("/project/build/plugins/plugin[1]/extensions", Boolean.toString(true));
	}

	@Test
	void kotlinAndSpringCloudContractVerifierPluginAddedWhenSCCDependencyPresent() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier");
		projectRequest.setLanguage("kotlin");
		projectRequest.setBootVersion("2.2.0.M2");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("cloud-contract-verifier"))
				.hasText("/project/dependencies/dependency[4]/groupId", "org.springframework.cloud")
				.hasText("/project/dependencies/dependency[4]/artifactId", "spring-cloud-contract-spec-kotlin")
				.hasText("/project/build/plugins/plugin[1]/groupId", "org.springframework.cloud")
				.hasText("/project/build/plugins/plugin[1]/artifactId", "spring-cloud-contract-maven-plugin")
				.hasText("/project/build/plugins/plugin[1]/extensions", Boolean.toString(true))
				.hasText("/project/build/plugins/plugin[1]/dependencies[1]/dependency/groupId",
						"org.springframework.cloud")
				.hasText("/project/build/plugins/plugin[1]/dependencies[1]/dependency/artifactId",
						"spring-cloud-contract-spec-kotlin");
	}

	@Test
	void springCloudContractVerifierPluginNotAddedWhenSCCDependencyAbsent() {
		ProjectRequest projectRequest = createProjectRequest();
		assertThat(mavenPom(projectRequest)).doesNotContain("spring-cloud-contract-maven-plugin");
	}

}
