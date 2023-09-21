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
 */
class JavaVersionProjectDescriptionCustomizerTests extends AbstractExtensionTests {

	@Test
	void javaUnknownVersionIsLeftAsIs() {
		assertThat(mavenPom(javaProject("9999999", "2.7.13"))).hasProperty("java.version", "9999999");
	}

	@Test
	void javaInvalidVersionIsLeftAsIs() {
		assertThat(mavenPom(javaProject("${another.version}", "2.7.13"))).hasProperty("java.version",
				"${another.version}");
	}

	@Test
	void java8IsNotCompatibleWithSpringBoot3() {
		ProjectRequest request = javaProject("1.8", "3.0.0");
		assertThat(mavenPom(request)).hasProperty("java.version", "17");
	}

	@Test
	void java11IsNotCompatibleWithSpringBoot3() {
		ProjectRequest request = javaProject("11", "3.0.0");
		assertThat(mavenPom(request)).hasProperty("java.version", "17");
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
		assertThat(gradleBuild(project(language, javaVersion, springBootVersion))).hasSourceCompatibility(javaVersion);
	}

	static Stream<Arguments> supportedMavenParameters() {
		return Stream.concat(supportedJavaParameters(),
				Stream.concat(supportedKotlinParameters(), supportedGroovyParameters()));
	}

	static Stream<Arguments> supportedGradleGroovyParameters() {
		return Stream.concat(supportedJavaParameters(), supportedGroovyParameters());
	}

	private static Stream<Arguments> supportedJavaParameters() {
		return Stream.of(java("19", "2.6.12"), java("20", "2.7.10"), java("21", "2.7.16"));
	}

	private static Stream<Arguments> supportedKotlinParameters() {
		return Stream.of(kotlin("17", "2.6.0"), kotlin("19", "2.6.12"), kotlin("20", "2.7.10"));
	}

	private static Stream<Arguments> supportedGroovyParameters() {
		return Stream.of(groovy("19", "2.6.12"), groovy("20", "2.7.10"), groovy("21", "2.7.16"));
	}

	@ParameterizedTest(name = "{0} - Java {1} - Spring Boot {2}")
	@MethodSource("unsupportedMavenParameters")
	void mavenBuildWithUnsupportedOptionsDowngradesToLts(String language, String javaVersion,
			String springBootVersion) {
		assertThat(mavenPom(project(language, javaVersion, springBootVersion))).hasProperty("java.version", "17");
	}

	@ParameterizedTest(name = "{0} - Java {1} - Spring Boot {2}")
	@MethodSource("unsupportedGradleGroovyParameters")
	void gradleGroovyBuildWithUnsupportedOptionsDowngradesToLts(String language, String javaVersion,
			String springBootVersion) {
		assertThat(gradleBuild(project(language, javaVersion, springBootVersion))).hasSourceCompatibility("17");
	}

	static Stream<Arguments> unsupportedMavenParameters() {
		return Stream.concat(unsupportedJavaParameters(),
				Stream.concat(unsupportedKotlinParameters(), unsupportedGroovyParameters()));
	}

	static Stream<Arguments> unsupportedGradleGroovyParameters() {
		return Stream.concat(unsupportedJavaParameters(), unsupportedGroovyParameters());
	}

	private static Stream<Arguments> unsupportedJavaParameters() {
		return Stream.of(java("19", "2.6.11"), java("20", "2.7.9"), java("21", "2.7.15"));
	}

	private static Stream<Arguments> unsupportedKotlinParameters() {
		return Stream.of(kotlin("19", "2.6.11"), kotlin("20", "2.7.9"), kotlin("21", "2.7.16"));
	}

	private static Stream<Arguments> unsupportedGroovyParameters() {
		return Stream.of(groovy("19", "2.6.11"), groovy("20", "2.7.9"), groovy("21", "2.7.15"));
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

}
