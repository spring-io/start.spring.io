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

package io.spring.start.site.extension.dependency.ldap;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LdapUnboundIdBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class LdapUnboundIdBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldAddUnboundIdIfBoot4orLaterIsUsed() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "unboundid-ldap");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-ldap", null, "test")
			.hasDependency("com.unboundid", "unboundid-ldapsdk", null, "test");
	}

	@Test
	void shouldNotAddSpringBootLdapIfNotUsingBoot4() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "unboundid-ldap");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot", "spring-boot-ldap")
			.hasDependency("com.unboundid", "unboundid-ldapsdk", null, "test");
	}

}
