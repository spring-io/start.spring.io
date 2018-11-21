/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import io.spring.initializr.web.project.WebProjectRequest;
import org.junit.Test;

/**
 * Tests for {@link JavaVersionProjectDescriptionCustomizer}.
 *
 * @author Stephane Nicoll
 */
public class JavaVersionProjectDescriptionCustomizerTests extends AbstractExtensionTests {

	@Test
	public void java9CannotBeUsedWithSpringBoot1Maven() {
		WebProjectRequest request = createProjectRequest("web");
		request.setBootVersion("1.5.8.RELEASE");
		request.setJavaVersion("9");
		generateMavenPom(request).hasJavaVersion("1.8");
	}

	@Test
	public void java9CannotBeUsedWithSpringBoot1Gradle() {
		WebProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("1.99.99.BUILD-SNAPSHOT");
		request.setJavaVersion("9");
		generateGradleBuild(request).hasJavaVersion("1.8");
	}

	@Test
	public void java9CannotBeUsedWithGroovyMaven() {
		WebProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.1.RELEASE");
		request.setLanguage("groovy");
		request.setJavaVersion("9");
		generateMavenPom(request).hasJavaVersion("1.8");
	}

	@Test
	public void java9CannotBeUsedWithKotlinMaven() {
		WebProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.1.RELEASE");
		request.setLanguage("kotlin");
		request.setJavaVersion("9");
		generateMavenPom(request).hasJavaVersion("1.8");
	}

	@Test
	public void java9CannotBeUsedWithGroovyGradle() {
		WebProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.1.RELEASE");
		request.setLanguage("groovy");
		request.setJavaVersion("9");
		generateGradleBuild(request).hasJavaVersion("1.8");
	}

	@Test
	public void java9CannotBeUsedWithKotlinGradle() {
		WebProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.1.RELEASE");
		request.setLanguage("kotlin");
		request.setJavaVersion("9");
		generateGradleBuild(request).hasJavaVersion("1.8");
	}

	@Test
	public void java9CanBeUsedWithSpringBoot2Maven() {
		WebProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.1.RELEASE");
		request.setJavaVersion("9");
		generateMavenPom(request).hasJavaVersion("9");
	}

	@Test
	public void java9CanBeUsedWithSpringBoot2Gradle() {
		WebProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.0.M3");
		request.setJavaVersion("9");
		generateGradleBuild(request).hasJavaVersion("9");
	}

	@Test
	public void java10CannotBeUsedWithSpringBoot1Maven() {
		WebProjectRequest request = createProjectRequest("web");
		request.setBootVersion("1.5.8.RELEASE");
		request.setJavaVersion("10");
		generateMavenPom(request).hasJavaVersion("1.8");
	}

	@Test
	public void java10CannotBeUsedWithSpringBoot1Gradle() {
		WebProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("1.99.99.BUILD-SNAPSHOT");
		request.setJavaVersion("10");
		generateGradleBuild(request).hasJavaVersion("1.8");
	}

	@Test
	public void java10CannotBeUsedWithSpringBoot200Maven() {
		WebProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.0.RELEASE");
		request.setJavaVersion("10");
		generateMavenPom(request).hasJavaVersion("1.8");
	}

	@Test
	public void java10CannotBeUsedWithSpringBoot200Gradle() {
		WebProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.0.RELEASE");
		request.setJavaVersion("10");
		generateGradleBuild(request).hasJavaVersion("1.8");
	}

	@Test
	public void java10CanBeUsedWithSpringBoot2Maven() {
		WebProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.1.RELEASE");
		request.setJavaVersion("10");
		generateMavenPom(request).hasJavaVersion("10");
	}

	@Test
	public void java10CanBeUsedWithSpringBoot2Gradle() {
		WebProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.2.RELEASE");
		request.setJavaVersion("10");
		generateGradleBuild(request).hasJavaVersion("10");
	}

	@Test
	public void java11CannotBeUsedWithSpringBoot1Maven() {
		WebProjectRequest request = createProjectRequest("web");
		request.setBootVersion("1.5.8.RELEASE");
		request.setJavaVersion("11");
		generateMavenPom(request).hasJavaVersion("1.8");
	}

	@Test
	public void java11CannotBeUsedWithSpringBoot1Gradle() {
		WebProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("1.99.99.BUILD-SNAPSHOT");
		request.setJavaVersion("11");
		generateGradleBuild(request).hasJavaVersion("1.8");
	}

	@Test
	public void java11CannotBeUsedWithSpringBoot20Maven() {
		WebProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.5.RELEASE");
		request.setJavaVersion("11");
		generateMavenPom(request).hasJavaVersion("1.8");
	}

	@Test
	public void java11CannotBeUsedWithSpringBoot20Gradle() {
		WebProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.5.RELEASE");
		request.setJavaVersion("11");
		generateGradleBuild(request).hasJavaVersion("1.8");
	}

	@Test
	public void java11CanBeUsedWithSpringBoot21Maven() {
		WebProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.1.0.M1");
		request.setJavaVersion("11");
		generateMavenPom(request).hasJavaVersion("11");
	}

	@Test
	public void java11CanBeUsedWithSpringBoot21Gradle() {
		WebProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.1.1.RELEASE");
		request.setJavaVersion("11");
		generateGradleBuild(request).hasJavaVersion("11");
	}

}
