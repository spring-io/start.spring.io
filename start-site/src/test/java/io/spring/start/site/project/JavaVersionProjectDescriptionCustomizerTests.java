/*
 * Copyright 2012 - present the original author or authors.
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

import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
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
		assertThat(mavenPom(javaProject("9999999", SupportedBootVersion.latest().getVersion())))
			.hasProperty("java.version", "9999999");
	}

	@Test
	void javaInvalidVersionIsLeftAsIs() {
		assertThat(mavenPom(javaProject("${another.version}", SupportedBootVersion.latest().getVersion())))
			.hasProperty("java.version", "${another.version}");
	}

	@Test
	void warningAddedWithUnsupportedCombination() {
		assertHelpDocument("11").lines()
			.containsSubsequence("# Read Me First",
					"* The JVM level was changed to '17', review the [JDK Version Range](https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-Versions#jdk-version-range) on the wiki for more details.");
	}

	@Test
	void warningAddedWithUnsupportedKotlinVersion() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.latest(), "web");
		request.setJavaVersion("26");
		request.setLanguage(KotlinLanguage.ID);
		assertHelpDocument(request).lines()
			.containsSubsequence("# Read Me First",
					"* The JVM level was changed to '25' as the Kotlin version does not support a later Java version yet.");
	}

	@Test
	void warningRefersToSpringBootWhenKotlinIsNotTheConstraint() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.latest(), "web");
		request.setJavaVersion("1.8");
		request.setLanguage(KotlinLanguage.ID);
		assertHelpDocument(request).lines()
			.containsSubsequence("# Read Me First",
					"* The JVM level was changed to '17', review the [JDK Version Range](https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-Versions#jdk-version-range) on the wiki for more details.");
	}

	@Test
	void warningNotAddedWithCompatibleVersion() {
		assertHelpDocument("17").doesNotContain("# Read Me First");
	}

	@ParameterizedTest(name = "{0} - Java {1}")
	@CsvSource(textBlock = """
			java,1.5
			java,1.6
			java,1.7
			java,1.8
			java,8
			java,11
			java,16
			kotlin,1.5
			kotlin,1.6
			kotlin,1.7
			kotlin,1.8
			kotlin,8
			kotlin,11
			kotlin,16
			groovy,1.5
			groovy,1.6
			groovy,1.7
			groovy,1.8
			groovy,8
			groovy,11
			groovy,16
			""")
	void belowMinimumIsRaisedToTheMinimum(String language, String jvmVersion) {
		assertThat(mavenPom(project(language, jvmVersion, SupportedBootVersion.latest().getVersion())))
			.hasProperty("java.version", "17");
	}

	@Test
	void kotlinIsCappedByTheKotlinVersionOfThePlatform() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "web");
		request.setJavaVersion("26");
		request.setLanguage(KotlinLanguage.ID);
		assertThat(mavenPom(request)).hasProperty("java.version", "24");
	}

	@Test
	void warningNotAddedWithUnparseableVersion() {
		assertHelpDocument("${another.version}").doesNotContain("# Read Me First");
	}

	private TextAssert assertHelpDocument(ProjectRequest request) {
		return assertThat(helpDocument(request));
	}

	private TextAssert assertHelpDocument(String jvmVersion) {
		ProjectRequest request = createProjectRequest("web");
		request.setType("gradle-project");
		request.setJavaVersion(jvmVersion);
		return assertHelpDocument(request);
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

	static Stream<Arguments> supportedMavenParameters() {
		return Stream.concat(supportedJavaParameters(),
				Stream.concat(supportedKotlinParameters(), supportedGroovyParameters()));
	}

	static Stream<Arguments> supportedGradleGroovyParameters() {
		return Stream.concat(supportedJavaParameters(), supportedGroovyParameters());
	}

	private static Stream<Arguments> supportedJavaParameters() {
		return Stream.of(java("17", SupportedBootVersion.latest().getVersion()),
				java("21", SupportedBootVersion.latest().getVersion()),
				java("25", SupportedBootVersion.latest().getVersion()),
				java("26", SupportedBootVersion.latest().getVersion()));
	}

	private static Stream<Arguments> supportedKotlinParameters() {
		return Stream.of(kotlin("21", SupportedBootVersion.latest().getVersion()));
	}

	private static Stream<Arguments> supportedGroovyParameters() {
		return Stream.of(groovy("21", SupportedBootVersion.latest().getVersion()),
				groovy("25", SupportedBootVersion.latest().getVersion()),
				groovy("26", SupportedBootVersion.latest().getVersion()));
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
