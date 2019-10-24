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

package io.spring.start.site.extension.dependency.springsecurity;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringSecurityRSocketBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class SpringSecurityRSocketBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void securityRSocketIsAddedWithSecurityAndRSocket() {
		ProjectRequest request = createProjectRequest("security", "rsocket");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("security"))
				.hasDependency(Dependency.createSpringBootStarter("rsocket"))
				.hasDependency("org.springframework.security", "spring-security-messaging")
				.hasDependency("org.springframework.security", "spring-security-rsocket");
	}

	@Test
	void securityRSocketIsNotAddedWithRSocketOnly() {
		ProjectRequest request = createProjectRequest("rsocket");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("rsocket"))
				.doesNotHaveDependency("org.springframework.security", "spring-security-messaging")
				.doesNotHaveDependency("org.springframework.security", "spring-security-rsocket");
	}

	@Test
	void securityRSocketIsNotAddedWithSecurityOnly() {
		ProjectRequest request = createProjectRequest("security");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("security"))
				.doesNotHaveDependency("org.springframework.security", "spring-security-messaging")
				.doesNotHaveDependency("org.springframework.security", "spring-security-rsocket");
	}

}
