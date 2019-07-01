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

package io.spring.start.site.extension.springboot;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DevToolsGradleBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class DevToolsGradleBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void gradleWithDevtoolsCreatesDevelopmentOnlyConfiguration() {
		ProjectRequest request = createProjectRequest("devtools");
		generateGradleBuild(request).contains("configurations {", "\tdevelopmentOnly", "\truntimeClasspath {",
				"\t\textendsFrom developmentOnly", "\t}", "}",
				"\tdevelopmentOnly 'org.springframework.boot:spring-boot-devtools'");
	}

	@Test
	void gradleWithoutDevtoolsDoesNotChangeOptional() {
		ProjectRequest request = createProjectRequest("web");
		generateGradleBuild(request).doesNotContain("developmentOnly");
	}

}
