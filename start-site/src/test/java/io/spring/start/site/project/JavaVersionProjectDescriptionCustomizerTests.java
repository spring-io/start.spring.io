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

package io.spring.start.site.project;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JavaVersionProjectDescriptionCustomizer}.
 *
 * @author Stephane Nicoll
 */
class JavaVersionProjectDescriptionCustomizerTests extends AbstractExtensionTests {

	@Test
	void java9CanBeUsedWithSpringBoot2Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.1.RELEASE");
		request.setJavaVersion("9");
		assertThat(mavenPom(request)).hasProperty("java.version", "9");
	}

	@Test
	void java9CanBeUsedWithSpringBoot2Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.0.RELEASE");
		request.setJavaVersion("9");
		assertThat(gradleBuild(request)).hasSourceCompatibility("9");
	}

	@Test
	void java10CannotBeUsedWithSpringBoot200Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.0.RELEASE");
		request.setJavaVersion("10");
		assertThat(mavenPom(request)).hasProperty("java.version", "1.8");
	}

	@Test
	void java10CannotBeUsedWithSpringBoot200Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.0.RELEASE");
		request.setJavaVersion("10");
		assertThat(gradleBuild(request)).hasSourceCompatibility("1.8");
	}

	@Test
	void java10CanBeUsedWithSpringBoot2Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.1.RELEASE");
		request.setJavaVersion("10");
		assertThat(mavenPom(request)).hasProperty("java.version", "10");
	}

	@Test
	void java10CanBeUsedWithSpringBoot2Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.2.RELEASE");
		request.setJavaVersion("10");
		assertThat(gradleBuild(request)).hasSourceCompatibility("10");
	}

	@Test
	void java11CannotBeUsedWithSpringBoot20Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.5.RELEASE");
		request.setJavaVersion("11");
		assertThat(mavenPom(request)).hasProperty("java.version", "1.8");
	}

	@Test
	void java11CannotBeUsedWithSpringBoot20Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.5.RELEASE");
		request.setJavaVersion("11");
		assertThat(gradleBuild(request)).hasSourceCompatibility("1.8");
	}

	@Test
	void java11CanBeUsedWithSpringBoot21Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.1.0.M1");
		request.setJavaVersion("11");
		assertThat(mavenPom(request)).hasProperty("java.version", "11");
	}

	@Test
	void java11CanBeUsedWithSpringBoot21Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.1.1.RELEASE");
		request.setJavaVersion("11");
		assertThat(gradleBuild(request)).hasSourceCompatibility("11");
	}

	@Test
	void java12CannotBeUsedWithSpringBoot20Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.0.5.RELEASE");
		request.setJavaVersion("12");
		assertThat(mavenPom(request)).hasProperty("java.version", "1.8");
	}

	@Test
	void java12CannotBeUsedWithSpringBoot20Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.0.5.RELEASE");
		request.setJavaVersion("12");
		assertThat(gradleBuild(request)).hasSourceCompatibility("1.8");
	}

	@Test
	void java12CanBeUsedWithSpringBoot21Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.1.0.M1");
		request.setJavaVersion("12");
		assertThat(mavenPom(request)).hasProperty("java.version", "12");
	}

	@Test
	void java12CanBeUsedWithSpringBoot21Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.1.1.RELEASE");
		request.setJavaVersion("12");
		assertThat(gradleBuild(request)).hasSourceCompatibility("12");
	}

	@Test
	void java12CanBeUsedWithSpringBoot22Kotlin() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setLanguage("kotlin");
		request.setJavaVersion("12");
		assertThat(mavenPom(request)).hasProperty("java.version", "12");
	}

	@Test
	void java13CannotBeUsedWithSpringBoot21Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.1.5.RELEASE");
		request.setJavaVersion("13");
		assertThat(mavenPom(request)).hasProperty("java.version", "11");
	}

	@Test
	void java13CannotBeUsedWithSpringBoot21Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.1.5.RELEASE");
		request.setJavaVersion("13");
		assertThat(gradleBuild(request)).hasSourceCompatibility("11");
	}

	@Test
	void java13CanBeUsedWithSpringBoot22Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("13");
		assertThat(mavenPom(request)).hasProperty("java.version", "13");
	}

	@Test // Gradle 5.x does not work with Java 13
	void java13CannotBeUsedWithSpringBoot220Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("13");
		assertThat(gradleBuild(request)).hasSourceCompatibility("11");
	}

	@Test // Gradle 6.x works with Java 13
	void java13CanBeUsedWithSpringBoot223Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.2.2.BUILD-SNAPSHOT");
		request.setJavaVersion("13");
		assertThat(gradleBuild(request)).hasSourceCompatibility("13");
	}

	@Test
	void java13CanBeUsedWithSpringBoot22Groovy() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setLanguage("groovy");
		request.setJavaVersion("13");
		assertThat(mavenPom(request)).hasProperty("java.version", "13");
	}

	@Test
	void java13CannotBeUsedWithKotlinMaven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setLanguage("kotlin");
		request.setJavaVersion("13");
		assertThat(mavenPom(request)).hasProperty("java.version", "11");
	}

	@Test
	void java14CannotBeUsedWithSpringBoot225Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.5.RELEASE");
		request.setJavaVersion("14");
		assertThat(mavenPom(request)).hasProperty("java.version", "11");
	}

	@Test
	void java14CannotBeUsedWithSpringBoot225Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.2.5.RELEASE");
		request.setJavaVersion("14");
		assertThat(gradleBuild(request)).hasSourceCompatibility("11");
	}

	@Test
	void java14CanBeUsedWithSpringBoot226Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.6.RELEASE");
		request.setJavaVersion("14");
		assertThat(mavenPom(request)).hasProperty("java.version", "14");
	}

	@Test
	void java14CanBeUsedWithSpringBoot226Gradle() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.6.RELEASE");
		request.setJavaVersion("14");
		assertThat(gradleBuild(request)).hasSourceCompatibility("14");
	}

	@Test
	void java14CanBeUsedWithSpringBoot22Groovy() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.6.RELEASE");
		request.setLanguage("groovy");
		request.setJavaVersion("14");
		assertThat(mavenPom(request)).hasProperty("java.version", "14");
	}

	@Test
	void java14CannotBeUsedWithKotlinMaven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setLanguage("kotlin");
		request.setJavaVersion("14");
		assertThat(mavenPom(request)).hasProperty("java.version", "11");
	}

}
