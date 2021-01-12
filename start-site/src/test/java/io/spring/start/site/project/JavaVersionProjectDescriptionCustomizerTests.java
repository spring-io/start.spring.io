/*
 * Copyright 2012-2021 the original author or authors.
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
	void java8IsMandatoryMaven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("1.7");
		assertThat(mavenPom(request)).hasProperty("java.version", "1.8");
	}

	@Test
	void java8IsMandatoryGradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("1.7");
		assertThat(gradleBuild(request)).hasSourceCompatibility("1.8");
	}

	@Test
	void java9CanBeUsedMaven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("9");
		assertThat(mavenPom(request)).hasProperty("java.version", "9");
	}

	@Test
	void java9CanBeUsedGradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("9");
		assertThat(gradleBuild(request)).hasSourceCompatibility("9");
	}

	@Test
	void java10CanBeUsedMaven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("10");
		assertThat(mavenPom(request)).hasProperty("java.version", "10");
	}

	@Test
	void java10CanBeUsedGradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("10");
		assertThat(gradleBuild(request)).hasSourceCompatibility("10");
	}

	@Test
	void java11CanBeUsedWithSpringBootMaven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("11");
		assertThat(mavenPom(request)).hasProperty("java.version", "11");
	}

	@Test
	void java11CanBeUsedWithSpringBootGradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("11");
		assertThat(gradleBuild(request)).hasSourceCompatibility("11");
	}

	@Test
	void java12CanBeUsedWithSpringBootMaven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("12");
		assertThat(mavenPom(request)).hasProperty("java.version", "12");
	}

	@Test
	void java12CanBeUsedWithSpringBootGradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.2.0.RELEASE");
		request.setJavaVersion("12");
		assertThat(gradleBuild(request)).hasSourceCompatibility("12");
	}

	@Test
	void java12CanBeUsedWithSpringBootKotlin() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setLanguage("kotlin");
		request.setJavaVersion("12");
		assertThat(mavenPom(request)).hasProperty("java.version", "12");
	}

	@Test
	void java13CanBeUsedWithSpringBootMaven() {
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
		request.setBootVersion("2.2.2.RELEASE");
		request.setJavaVersion("13");
		assertThat(gradleBuild(request)).hasSourceCompatibility("13");
	}

	@Test
	void java13CanBeUsedWithSpringBootGroovy() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setLanguage("groovy");
		request.setJavaVersion("13");
		assertThat(mavenPom(request)).hasProperty("java.version", "13");
	}

	@Test
	void java13CanBeUsedWithKotlinMaven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.0.RELEASE");
		request.setLanguage("kotlin");
		request.setJavaVersion("13");
		assertThat(mavenPom(request)).hasProperty("java.version", "13");
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
	void java14CanBeUsedWithKotlinMaven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.6.RELEASE");
		request.setLanguage("kotlin");
		request.setJavaVersion("14");
		assertThat(mavenPom(request)).hasProperty("java.version", "14");
	}

	@Test
	void java15CannotBeUsedWithSpringBoot2210Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.2.10.RELEASE");
		request.setJavaVersion("15");
		assertThat(mavenPom(request)).hasProperty("java.version", "11");
	}

	@Test
	void java15CannotBeUsedWithSpringBoot2210Gradle() {
		ProjectRequest request = createProjectRequest("data-jpa");
		request.setBootVersion("2.2.10.RELEASE");
		request.setJavaVersion("15");
		assertThat(gradleBuild(request)).hasSourceCompatibility("11");
	}

	@Test
	void java145anBeUsedWithSpringBoot2211Maven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.3.4.RELEASE");
		request.setJavaVersion("15");
		assertThat(mavenPom(request)).hasProperty("java.version", "15");
	}

	@Test
	void java15CanBeUsedWithSpringBoot2211Gradle() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.3.4.RELEASE");
		request.setJavaVersion("15");
		assertThat(gradleBuild(request)).hasSourceCompatibility("15");
	}

	@Test
	void java15CanBeUsedWithSpringBoot22Groovy() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.3.4.RELEASE");
		request.setLanguage("groovy");
		request.setJavaVersion("15");
		assertThat(mavenPom(request)).hasProperty("java.version", "15");
	}

	@Test
	void java15CantBeUsedWithKotlinMaven() {
		ProjectRequest request = createProjectRequest("web");
		request.setBootVersion("2.3.4.RELEASE");
		request.setLanguage("kotlin");
		request.setJavaVersion("15");
		assertThat(mavenPom(request)).hasProperty("java.version", "15");
	}

}
