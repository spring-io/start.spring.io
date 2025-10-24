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

package io.spring.start.site.extension.dependency.springsecurity;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringSecurityTestBuildCustomizer}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class SpringSecurityTestBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldAddTestDependencyForSecurity() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "security");
		assertThat(mavenPom(request)).hasDependency(springSecurityTest());
	}

	@Test
	void shouldAddTestDependencyForOauth2Client() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "oauth2-client");
		assertThat(mavenPom(request)).hasDependency(springSecurityTest());
	}

	@Test
	void shouldNotAddTestDependencyIfSecurityIsNotSelected() {
		Dependency dependency = springSecurityTest();
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "web");
		assertThat(mavenPom(request)).doesNotHaveDependency(dependency.getArtifactId(), dependency.getGroupId());
	}

	@Test
	void shouldNotAddTestDependencyForBoot4() {
		Dependency dependency = springSecurityTest();
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "security");
		assertThat(mavenPom(request)).doesNotHaveDependency(dependency.getArtifactId(), dependency.getGroupId());
	}

	private static Dependency springSecurityTest() {
		Dependency dependency = Dependency.withId("spring-security-test", "org.springframework.security",
				"spring-security-test");
		dependency.setScope(Dependency.SCOPE_TEST);
		return dependency;
	}

}
