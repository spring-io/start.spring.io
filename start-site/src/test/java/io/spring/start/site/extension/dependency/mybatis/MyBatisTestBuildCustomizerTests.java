/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.mybatis;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MyBatisTestBuildCustomizer}.
 *
 * @author Kazuki Shimizu
 */
class MyBatisTestBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	@Test
	void mybatisIsAddedWithSecurity() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "mybatis");
		assertThat(mavenPom(request)).hasDependency(mybatis())
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependency(mybatisTest())
			.hasDependenciesSize(3);
	}

	@Test
	void mybatisTestIsNotAddedWithoutMyBatis() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web");
		Dependency dependency = mybatisTest();
		assertThat(mavenPom(request)).doesNotHaveDependency(dependency.getGroupId(), dependency.getArtifactId());
	}

	private static Dependency mybatis() {
		return Dependency.withId("mybatis", "org.mybatis.spring.boot", "mybatis-spring-boot-starter");
	}

	private static Dependency mybatisTest() {
		Dependency dependency = Dependency.withId("mybatis-test", "org.mybatis.spring.boot",
				"mybatis-spring-boot-starter-test");
		dependency.setScope(Dependency.SCOPE_TEST);
		return dependency;
	}

}
