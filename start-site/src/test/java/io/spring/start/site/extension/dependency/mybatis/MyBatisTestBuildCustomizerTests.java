/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.mybatis;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MyBatisTestBuildCustomizer}.
 *
 * @author Kazuki Shimizu
 */
class MyBatisTestBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void mybatisIsAddedWithSecurity() {
		ProjectRequest request = createProjectRequest("mybatis");
		request.setBootVersion("3.1.0");
		assertThat(mavenPom(request)).hasDependency(mybatis())
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependency(mybatisTest())
			.hasDependenciesSize(3);
	}

	@Test
	void mybatisTestIsNotAddedWithoutMyBatis() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("web"))
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependenciesSize(2);
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
