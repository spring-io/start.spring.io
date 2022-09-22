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
	void java8IsMandatoryMaven() {
		assertThat(mavenPom(javaProject("1.7", "2.3.0.RELEASE"))).hasProperty("java.version", "1.8");
	}

	@Test
	void java8IsMandatoryGradle() {
		assertThat(gradleBuild(javaProject("1.7", "2.3.0.RELEASE"))).hasSourceCompatibility("1.8");
	}

	@Test
	void javaUnknownVersionIsLeftAsIs() {
		assertThat(mavenPom(javaProject("9999999", "2.3.0.RELEASE"))).hasProperty("java.version", "9999999");
	}

	@Test
	void javaInvalidVersionIsLeftAsIs() {
		assertThat(mavenPom(javaProject("${another.version}", "2.3.0.RELEASE"))).hasProperty("java.version",
				"${another.version}");
	}

	@Test
	void java8IsCompatibleWithSpringNative010() {
		ProjectRequest request = javaProject("1.8", "2.5.5");
		request.getDependencies().add("native");
		assertThat(mavenPom(request)).hasProperty("java.version", "1.8");
	}

	@Test
	void java8IsNotCompatibleWithSpringNative011() {
		ProjectRequest request = javaProject("1.8", "2.6.0-M3");
		request.getDependencies().add("native");
		assertThat(mavenPom(request)).hasProperty("java.version", "11");
	}

	@Test
	void java8WithoutSpringNative011IsNotDowngraded() {
		ProjectRequest request = javaProject("1.8", "2.6.0-M3");
		request.getDependencies().add("web");
		assertThat(mavenPom(request)).hasProperty("java.version", "1.8");
	}

	@Test
	void java8IsNotCompatibleWithSpringBoot3() {
		ProjectRequest request = javaProject("1.8", "3.0.0-M1");
		assertThat(mavenPom(request)).hasProperty("java.version", "17");
	}

	@Test
	void java11IsNotCompatibleWithSpringBoot3() {
		ProjectRequest request = javaProject("11", "3.0.0-M1");
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
		return Stream.of(java("9", "2.3.0.RELEASE"), java("10", "2.3.0.RELEASE"), java("11", "2.3.0.RELEASE"),
				java("12", "2.3.0.RELEASE"), java("13", "2.3.0.RELEASE"), java("14", "2.3.0.RELEASE"),
				java("15", "2.3.4.RELEASE"), java("16", "2.5.0-RC1"), java("17", "2.5.5"), java("18", "2.5.11"),
				java("19", "2.6.12"));
	}

	private static Stream<Arguments> supportedKotlinParameters() {
		return Stream.of(kotlin("9", "2.3.0.RELEASE"), kotlin("10", "2.3.0.RELEASE"), kotlin("11", "2.3.0.RELEASE"),
				kotlin("12", "2.3.0.RELEASE"), kotlin("13", "2.3.0.RELEASE"), kotlin("14", "2.3.0.RELEASE"),
				kotlin("15", "2.3.4.RELEASE"), kotlin("16", "2.5.0-RC1"), kotlin("17", "2.6.0"));
	}

	private static Stream<Arguments> supportedGroovyParameters() {
		return Stream.of(groovy("9", "2.3.0.RELEASE"), groovy("10", "2.3.0.RELEASE"), groovy("11", "2.3.0.RELEASE"),
				groovy("12", "2.3.0.RELEASE"), groovy("13", "2.3.0.RELEASE"), groovy("14", "2.3.0.RELEASE"),
				groovy("15", "2.3.4.RELEASE"), groovy("16", "2.5.0-RC1"), groovy("17", "2.5.5"), groovy("18", "2.5.11"),
				groovy("19", "2.6.12"));
	}

	@ParameterizedTest(name = "{0} - Java {1} - Spring Boot {2}")
	@MethodSource("unsupportedMavenParameters")
	void mavenBuildWithUnsupportedOptionsDowngradesToLts(String language, String javaVersion,
			String springBootVersion) {
		assertThat(mavenPom(project(language, javaVersion, springBootVersion))).hasProperty("java.version", "11");
	}

	@ParameterizedTest(name = "{0} - Java {1} - Spring Boot {2}")
	@MethodSource("unsupportedGradleGroovyParameters")
	void gradleGroovyBuildWithUnsupportedOptionsDowngradesToLts(String language, String javaVersion,
			String springBootVersion) {
		assertThat(gradleBuild(project(language, javaVersion, springBootVersion))).hasSourceCompatibility("11");
	}

	static Stream<Arguments> unsupportedMavenParameters() {
		return Stream.concat(unsupportedJavaParameters(),
				Stream.concat(unsupportedKotlinParameters(), unsupportedGroovyParameters()));
	}

	static Stream<Arguments> unsupportedGradleGroovyParameters() {
		return Stream.concat(unsupportedJavaParameters(), unsupportedGroovyParameters());
	}

	private static Stream<Arguments> unsupportedJavaParameters() {
		return Stream.of(java("16", "2.4.3"), java("17", "2.5.4"));
	}

	private static Stream<Arguments> unsupportedKotlinParameters() {
		return Stream.of(kotlin("16", "2.4.3"), kotlin("17", "2.5.5"), kotlin("18", "2.5.11"), kotlin("19", "2.5.11"));
	}

	private static Stream<Arguments> unsupportedGroovyParameters() {
		return Stream.of(groovy("16", "2.4.3"), groovy("17", "2.5.4"));
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
