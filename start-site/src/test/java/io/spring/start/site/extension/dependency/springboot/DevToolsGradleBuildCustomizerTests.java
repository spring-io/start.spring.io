/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.start.site.extension.dependency.springboot;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DevToolsGradleBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class DevToolsGradleBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void gradleWithDevtoolsAndSpringBootPriorTo23RC1CreatesDevelopmentOnlyConfiguration() {
		ProjectRequest request = createProjectRequest("devtools");
		request.setBootVersion("2.3.0.M4");
		assertThat(gradleBuild(request)).contains("configurations {", "\tdevelopmentOnly", "\truntimeClasspath {",
				"\t\textendsFrom developmentOnly", "\t}", "}",
				"\tdevelopmentOnly 'org.springframework.boot:spring-boot-devtools'");
	}

	@Test
	void gradleWithDevtoolsAndSpringBoot23SnapshotsDoesNotCreateDevelopmentOnlyConfiguration() {
		ProjectRequest request = createProjectRequest("devtools");
		request.setBootVersion("2.3.0.BUILD-SNAPSHOT");
		assertThat(gradleBuild(request)).lines().doesNotContain("configurations {")
				.contains("\tdevelopmentOnly 'org.springframework.boot:spring-boot-devtools'");
	}

	@Test
	void gradleWithoutDevtoolsAndSpringBootPriorTo23RC1DoesNotCreateDevelopmentOnlyConfiguration() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.3.0.M4");
		assertThat(gradleBuild(request)).doesNotContain("developmentOnly");
	}

}
