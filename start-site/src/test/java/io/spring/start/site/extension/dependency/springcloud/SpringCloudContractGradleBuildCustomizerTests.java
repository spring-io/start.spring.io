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

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringCloudContractGradleBuildCustomizer}.
 *
 * @author Olga Maciaszek-Sharma
 * @author Madhura Bhave
 * @author Eddú Meléndez
 */
class SpringCloudContractGradleBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void springCloudContractVerifierPluginAddedWhenSCCDependencyPresent() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier");
		assertThat(gradleBuild(projectRequest))
				.containsSubsequence("buildscript {", "dependencies {",
						"classpath 'org.springframework.cloud:spring-cloud-contract-gradle-plugin:")
				.contains("apply plugin: 'spring-cloud-contract'");
	}

	@Test
	void springCloudContractVerifierPluginNotAddedWhenSCCDependencyAbsent() {
		ProjectRequest projectRequest = createProjectRequest();
		assertThat(gradleBuild(projectRequest))
				.doesNotContain("buildscript {",
						"classpath 'org.springframework.cloud:spring-cloud-contract-gradle-plugin:")
				.doesNotContain("apply plugin: 'spring-cloud-contract'");
	}

	@Test
	void springCloudContractVerifierPluginForSpringBootWithJUnit5ByDefault() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier");
		projectRequest.setBootVersion("2.4.0");
		assertThat(gradleBuild(projectRequest)).containsSubsequence("tasks.named('contracts') {",
				"testFramework = org.springframework.cloud.contract.verifier.config.TestFramework.JUNIT5");
	}

	@Test
	void springCloudContractVerifierPlugin2WithNoContractTestConfiguration() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier");
		projectRequest.setBootVersion("2.3.7.RELEASE");
		assertThat(gradleBuild(projectRequest)).doesNotContain("tasks.named('contractTest') {");
	}

	@Test
	void springCloudContractVerifierPlugin30ContractTestWithJUnit5ByDefault() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier");
		projectRequest.setBootVersion("2.4.1");
		assertThat(gradleBuild(projectRequest)).containsSubsequence("tasks.named('contractTest') {",
				"useJUnitPlatform()");
	}

	@Test
	void springCloudContractVerifierPluginWithTestModeSetWhenWebFluxIsPresent() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier", "webflux");
		assertThat(gradleBuild(projectRequest)).containsSubsequence("tasks.named('contracts') {",
				"testMode = 'WebTestClient'");
	}

	@Test
	void springWebTestClientDependencyAddedWhenWebFluxIsPresent() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier", "webflux");
		assertThat(gradleBuild(projectRequest)).contains("testImplementation 'io.rest-assured:spring-web-test-client'");
	}

}
