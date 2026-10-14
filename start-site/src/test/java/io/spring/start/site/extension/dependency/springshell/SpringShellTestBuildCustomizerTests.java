/*
 * Copyright 2012 - present the original author or authors.
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

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

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
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web");
		Dependency dependency = springShellStarterTest();
		assertThat(mavenPom(request)).doesNotHaveDependency(dependency.getGroupId(), dependency.getArtifactId());
	}

	private static Dependency springShellStarterTest() {
		Dependency dependency = Dependency.withId("spring-shell-starter-test", "org.springframework.shell",
				"spring-shell-starter-test");
		dependency.setScope(Dependency.SCOPE_TEST);
		return dependency;
	}

}
