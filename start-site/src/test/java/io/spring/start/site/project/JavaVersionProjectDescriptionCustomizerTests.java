/*
 * Copyright 2012-2024 the original author or authors.
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

import java.util.stream.Stream;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JavaVersionProjectDescriptionCustomizer}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class JavaVersionProjectDescriptionCustomizerTests extends AbstractExtensionTests {

	@Test
	void javaUnknownVersionIsLeftAsIs() {
		assertThat(mavenPom(javaProject("9999999", "3.3.0"))).hasProperty("java.version", "9999999");
	}

	@Test
	void javaInvalidVersionIsLeftAsIs() {
		assertThat(mavenPom(javaProject("${another.version}", "3.3.0"))).hasProperty("java.version",
				"${another.version}");
	}

	@ParameterizedTest(name = "{0} - Java {1} - Spring Boot {2}")
	@MethodSource("supportedMavenParameters")
	void mavenBuildWithSupportedOptionsDoesNotDowngradeJavaVersion(String language, String javaVersion,
			String springBootVersion) {
		assertThat(mavenPom(project(language, javaVersion, springBootVersion))).hasProperty("java.version",
				javaVersion);
	}

	@ParameterizedTest(name = "{0} - Java {1} - Spring Boot {2}")
	@MethodSource("supportedGradleGroovyParameters")
	void gradleGroovyBuildWithSupportedOptionsDoesNotDowngradeJavaVersion(String language, String javaVersion,
			String springBootVersion) {
		assertThat(gradleBuild(project(language, javaVersion, springBootVersion))).hasToolchainForJava(javaVersion);
	}

	@Test
	void java22IsNotSupportedWithBoot323() {
		assertThat(mavenPom(javaProject("22", "3.2.3"))).hasProperty("java.version", "21");
	}

	@Test
	void java22IsNotSupportedWithKotlin() {
		assertThat(mavenPom(kotlinProject("22", "3.2.5"))).hasProperty("java.version", "21");
	}

	static Stream<Arguments> supportedMavenParameters() {
		return Stream.concat(supportedJavaParameters(),
				Stream.concat(supportedKotlinParameters(), supportedGroovyParameters()));
	}

	static Stream<Arguments> supportedGradleGroovyParameters() {
		return Stream.concat(supportedJavaParameters(), supportedGroovyParameters());
	}

	private static Stream<Arguments> supportedJavaParameters() {
		return Stream.of(java("17", "3.2.0"), java("21", "3.2.0"), java("22", "3.2.4"), java("17", "3.3.0"),
				java("21", "3.3.0"), java("22", "3.3.0"));
	}

	private static Stream<Arguments> supportedKotlinParameters() {
		return Stream.of(kotlin("21", "3.2.0"), kotlin("21", "3.3.0"));
	}

	private static Stream<Arguments> supportedGroovyParameters() {
		return Stream.of(groovy("21", "3.2.0"), groovy("22", "3.2.4"), groovy("21", "3.3.0-M2"),
				groovy("22", "3.3.0-M3"));
	}

	private static Arguments java(String javaVersion, String springBootVersion) {
		return Arguments.of("java", javaVersion, springBootVersion);
	}

	private static Arguments kotlin(String javaVersion, String springBootVersion) {
		return Arguments.of("kotlin", javaVersion, springBootVersion);
	}

	private static Arguments groovy(String javaVersion, String springBootVersion) {
		return Arguments.of("groovy", javaVersion, springBootVersion);
	}

	private ProjectRequest project(String language, String javaVersion, String springBootVersion) {
		ProjectRequest request = createProjectRequest("web");
		request.setLanguage(language);
		request.setJavaVersion(javaVersion);
		request.setBootVersion(springBootVersion);
		return request;
	}

	private ProjectRequest javaProject(String javaVersion, String springBootVersion) {
		return project("java", javaVersion, springBootVersion);
	}

	private ProjectRequest kotlinProject(String javaVersion, String springBootVersion) {
		return project("kotlin", javaVersion, springBootVersion);
	}

}
