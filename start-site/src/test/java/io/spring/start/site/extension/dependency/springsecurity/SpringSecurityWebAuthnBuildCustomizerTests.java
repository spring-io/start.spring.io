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
 * Tests for {@link SpringSecurityWebAuthnBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class SpringSecurityWebAuthnBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldDoNothingIfNotSelected() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "web");
		Dependency security = getDependency("security");
		assertThat(mavenPom(request)).doesNotHaveDependency(security.getGroupId(), security.getArtifactId());
	}

	@Test
	void shouldAddSpringSecurityOnBoot40() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "spring-security-webauthn");
		assertThat(mavenPom(request)).hasDependency(getDependency("security"));
	}

}
