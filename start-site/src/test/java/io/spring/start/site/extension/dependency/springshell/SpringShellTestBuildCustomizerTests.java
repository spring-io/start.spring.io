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

package io.spring.start.site.extension.dependency.springshell;

import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringShellTestBuildCustomizer}.
 *
 * @author DaShaun Carter
 */
class SpringShellTestBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V3_4;

	@Test
	void shellTestIsAddedWithSpringShell() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "spring-shell");
		BillOfMaterials bom = getBom("spring-shell", request.getBootVersion());
		assertThat(mavenPom(request)).hasDependency(getDependency("spring-shell"))
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependency(springShellStarterTest())
			.hasDependenciesSize(3)
			.hasBom("org.springframework.shell", "spring-shell-dependencies", "${spring-shell.version}")
			.hasBomsSize(1)
			.hasProperty("spring-shell.version", bom.getVersion());
	}

	@Test
	void shellTestIsNotAddedWithoutSpringShell() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("web"))
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependenciesSize(2);
	}

	private static Dependency springShellStarterTest() {
		Dependency dependency = Dependency.withId("spring-shell-starter-test", "org.springframework.shell",
				"spring-shell-starter-test");
		dependency.setScope(Dependency.SCOPE_TEST);
		return dependency;
	}

}
