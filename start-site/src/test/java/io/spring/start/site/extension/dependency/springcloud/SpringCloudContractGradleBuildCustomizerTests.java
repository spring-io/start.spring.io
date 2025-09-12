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
		ProjectRequest request = createProjectRequest("cloud-contract-verifier");
		assertThat(gradleBuild(request)).contains("id 'org.springframework.cloud.contract' version '");
	}

	@Test
	void springCloudContractVerifierPluginNotAddedWhenSCCDependencyAbsent() {
		ProjectRequest request = createProjectRequest();
		assertThat(gradleBuild(request)).doesNotContain("org.springframework.cloud.contract");
	}

	@Test
	void springCloudContractVerifierPluginContractTestWithJUnit5ByDefault() {
		ProjectRequest request = createProjectRequest("cloud-contract-verifier");
		assertThat(gradleBuild(request)).containsSubsequence("tasks.named('contractTest') {", "useJUnitPlatform()");
	}

	@Test
	void springCloudContractVerifierPluginWithGroovyDslAndWithTestModeSetWhenWebFluxIsPresent() {
		ProjectRequest request = createProjectRequest("cloud-contract-verifier", "webflux");
		assertThat(gradleBuild(request)).containsSubsequence("contracts {", "testMode = 'WebTestClient'");
	}

	@Test
	void springCloudContractVerifierPluginWithKotlinDslAndTestModeSetWhenWebFluxIsPresent() {
		ProjectRequest request = createProjectRequest("cloud-contract-verifier", "webflux");
		assertThat(gradleKotlinDslBuild(request))
			.contains("import org.springframework.cloud.contract.verifier.config.TestMode")
			.containsSubsequence("contracts {", "testMode = TestMode.WEBTESTCLIENT");
	}

	@Test
	void springWebTestClientDependencyAddedWhenWebFluxIsPresent() {
		ProjectRequest request = createProjectRequest("cloud-contract-verifier", "webflux");
		assertThat(gradleBuild(request)).contains("testImplementation 'io.rest-assured:spring-web-test-client'");
	}

}
