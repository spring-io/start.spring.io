/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.start.site.extension.dependency.thymeleaf;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ThymeleafBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class ThymeleafBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void thymeleafWithSpringSecurityAndSpringBoot2AddsExtrasDependency() {
		ProjectRequest projectRequest = createProjectRequest("thymeleaf", "security");
		projectRequest.setBootVersion("2.7.1");
		assertThat(mavenPom(projectRequest)).hasDependency(Dependency.createSpringBootStarter("thymeleaf"))
				.hasDependency(Dependency.createSpringBootStarter("security"))
				.hasDependency(Dependency.withId("thymeleaf-extras-spring-security", "org.thymeleaf.extras",
						"thymeleaf-extras-springsecurity5"));
	}

	@Test
	void thymeleafWithSpringSecurityAndSpringBoot3AddsExtrasDependency() {
		ProjectRequest projectRequest = createProjectRequest("thymeleaf", "security");
		projectRequest.setBootVersion("3.0.0-M1");
		assertThat(mavenPom(projectRequest)).hasDependency(Dependency.createSpringBootStarter("thymeleaf"))
				.hasDependency(Dependency.createSpringBootStarter("security"))
				.hasDependency(Dependency.withId("thymeleaf-extras-spring-security", "org.thymeleaf.extras",
						"thymeleaf-extras-springsecurity6"));
	}

	@Test
	void thymeleafWithoutSpringSecurityDoesNotAddExtrasDependency() {
		assertThat(mavenPom(createProjectRequest("thymeleaf", "web")))
				.hasDependency(Dependency.createSpringBootStarter("thymeleaf"))
				.doesNotHaveDependency("org.thymeleaf.extras", "thymeleaf-extras-springsecurity5");
	}

}
