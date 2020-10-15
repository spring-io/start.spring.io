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

package io.spring.start.site.extension.code.groovy;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GroovyProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 */
class GroovyProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void groovyProjectWithJava8() {
		ProjectRequest request = groovyProjectRequest();
		request.setJavaVersion("1.8");
		assertThat(mavenPom(request)).doesNotHaveProperty("groovy.version");
	}

	@Test
	void groovyProjectWithJava14() {
		ProjectRequest request = groovyProjectRequest();
		request.setJavaVersion("14");
		assertThat(mavenPom(request)).hasProperty("groovy.version", "3.0.6");
	}

	@Test
	void groovyProjectWithJava15() {
		ProjectRequest request = groovyProjectRequest();
		request.setJavaVersion("15");
		assertThat(mavenPom(request)).hasProperty("groovy.version", "3.0.6");
	}

	private ProjectRequest groovyProjectRequest() {
		ProjectRequest request = createProjectRequest();
		request.setLanguage("groovy");
		request.setBootVersion("2.3.4.RELEASE");
		return request;
	}

}
