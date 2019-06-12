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

import io.spring.initializr.web.project.ProjectRequest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LombokGradleBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class LombokGradleBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void lombokConfiguredWithCompileOnlyScope() {
		ProjectRequest request = createProjectRequest("lombok");
		generateGradleBuild(request).contains("annotationProcessor 'org.projectlombok:lombok'")
				.contains("compileOnly 'org.projectlombok:lombok'");
	}

	@Test
	void lombokNotAddedIfLombokIsNotSelected() {
		ProjectRequest request = createProjectRequest("web");
		generateGradleBuild(request).doesNotContain("compileOnly 'org.projectlombok:lombok'");
	}

	@Test
	void lombokNotConfiguredWithSpringBoot15() {
		ProjectRequest request = createProjectRequest("lombok");
		request.setBootVersion("1.5.18.RELEASE");
		generateGradleBuild(request).containsOnlyOnce("compileOnly 'org.projectlombok:lombok'");
	}

}
