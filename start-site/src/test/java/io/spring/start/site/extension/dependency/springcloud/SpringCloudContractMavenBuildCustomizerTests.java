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

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.metadata.Dependency;
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
	void springCloudContractVerifierPluginNotAddedWhenSCCDependencyAbsent() {
		ProjectRequest projectRequest = createProjectRequest();
		assertThat(mavenPom(projectRequest)).doesNotContain("spring-cloud-contract-maven-plugin");
	}

	@Test
	void springCloudContractVerifierPluginForSpringBootWithJUnit5ByDefault() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier");
		projectRequest.setBootVersion("2.4.0");
		assertThat(mavenPom(projectRequest))
				.hasText("/project/build/plugins/plugin[1]/artifactId", "spring-cloud-contract-maven-plugin")
				.hasText("/project/build/plugins/plugin[1]/configuration/testFramework", "JUNIT5");
	}

	@Test
	void springCloudContractVerifierPluginWithTestModeSetWhenWebFluxIsPresent() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier", "webflux");
		assertThat(mavenPom(projectRequest))
				.hasText("/project/build/plugins/plugin[1]/artifactId", "spring-cloud-contract-maven-plugin")
				.hasText("/project/build/plugins/plugin[1]/configuration/testMode", "WEBTESTCLIENT");
	}

	@Test
	void springWebTestClientDependencyAddedWhenWebFluxIsPresent() {
		ProjectRequest projectRequest = createProjectRequest("cloud-contract-verifier", "webflux");
		Dependency springWebTestClientDep = Dependency.withId("rest-assured-spring-web-test-client", "io.rest-assured",
				"spring-web-test-client");
		springWebTestClientDep.setScope(Dependency.SCOPE_TEST);
		assertThat(mavenPom(projectRequest)).hasDependency(springWebTestClientDep);
	}

}
