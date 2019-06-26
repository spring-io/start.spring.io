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

package io.spring.start.site.extension;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SpringLdapBuildCustomizer}.
 *
 * @author Eddú Meléndez
 */
class SpringLdapBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void unboundidIsAddedWithSpringLdap() {
		ProjectRequest request = createProjectRequest("data-ldap");
		request.setBootVersion("1.5.0.RELEASE");
		Dependency unboundid = Dependency.withId("unboundid-ldapsdk", "com.unboundid", "unboundid-ldapsdk", null,
				Dependency.SCOPE_TEST);
		generateMavenPom(request).hasSpringBootStarterTest().hasDependency(unboundid).hasDependenciesCount(3);
	}

	@Test
	void unboundidIsNotAddedWithoutSpringLdap() {
		ProjectRequest request = createProjectRequest("web");
		generateMavenPom(request).hasSpringBootStarterDependency("web").hasSpringBootStarterTest()
				.hasDependenciesCount(2);
	}

}
