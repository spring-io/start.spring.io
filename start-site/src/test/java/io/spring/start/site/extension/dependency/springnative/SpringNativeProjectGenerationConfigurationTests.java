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

package io.spring.start.site.extension.dependency.springnative;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringNativeProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 */
class SpringNativeProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private final String springNativeVersion;

	SpringNativeProjectGenerationConfigurationTests(@Autowired InitializrMetadataProvider metadataProvider) {
		this.springNativeVersion = determineSpringNativeVersion(metadataProvider.get());
	}

	private static String determineSpringNativeVersion(InitializrMetadata metadata) {
		return metadata.getDependencies().get("native")
				.resolve(Version.parse(metadata.getBootVersions().getDefault().getId())).getVersion();
	}

	@Test
	void gradleBuildWithoutNativeDoesNotConfigureNativeSupport() {
		assertThat(gradleBuild(createProjectRequest("web"))).doesNotContain("springNativeVersion");
	}

	@Test
	void mavenBuildWithoutNativeDoesNotConfigureNativeSupport() {
		assertThat(mavenPom(createProjectRequest("web"))).doesNotContain("spring-native.version");
	}

	@Test
	void mavenBuildSetProperty() {
		assertThat(mavenPom(createProjectRequest("native"))).hasProperty("spring-native.version",
				this.springNativeVersion);
	}

	@Test
	void gradleBuildDoesNotContainExplicitSpringNativeDependency() {
		assertThat(gradleBuild(createProjectRequest("native")))
				.doesNotContain("org.springframework.experimental:spring-native");
	}

	@Test
	void mavenBuildReusePropertyForSpringNativeDependency() {
		assertThat(mavenPom(createProjectRequest("native"))).hasDependency("org.springframework.experimental",
				"spring-native", "${spring-native.version}");
	}

	@Test
	void gradleBuildConfigureMavenCentral() {
		ProjectRequest request = createNativeProjectRequest();
		request.setType("gradle-project");
		assertThat(generateProject(request)).containsFiles("settings.gradle").textFile("settings.gradle")
				.containsSubsequence("pluginManagement {", "repositories {",
						"maven { url 'https://repo.spring.io/release' }", "mavenCentral", "gradlePluginPortal()", "}",
						"}");
	}

	@Test
	void gradleBuildConfigureAotPlugin() {
		assertThat(gradleBuild(createProjectRequest("native"))).hasPlugin("org.springframework.experimental.aot",
				this.springNativeVersion);
	}

	@Test
	void gradleBuildOnlyRefersToSpringNativeVersionOnce() {
		assertThat(gradleBuild(createProjectRequest("native"))).containsOnlyOnce(this.springNativeVersion);
	}

	@Test
	void mavenBuildConfigureAotPlugin() {
		assertThat(mavenPom(createProjectRequest("native"))).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>org.springframework.experimental</groupId>",
				"				<artifactId>spring-aot-maven-plugin</artifactId>",
				"				<version>${spring-native.version}</version>",
				"				<executions>",
				"					<execution>",
				"						<id>test-generate</id>",
				"						<goals>",
				"							<goal>test-generate</goal>",
				"						</goals>",
				"					</execution>",
				"					<execution>",
				"						<id>generate</id>",
				"						<goals>",
				"							<goal>generate</goal>",
				"						</goals>",
				"					</execution>",
				"				</executions>",
				"			</plugin>");
		// @formatter:on
	}

	@Test
	void gradleBuildConfigureSpringBootPlugin() {
		assertThat(gradleBuild(createProjectRequest("native"))).lines().containsSequence(
				"tasks.named('bootBuildImage') {", "	builder = 'paketobuildpacks/builder:tiny'",
				"	environment = ['BP_NATIVE_IMAGE': 'true']", "}");
	}

	@Test
	void gradleBuildWithoutJpaDoesNotConfigureHibernateEnhancePlugin() {
		assertThat(gradleBuild(createProjectRequest("native"))).doesNotContain("org.hibernate.orm");
	}

	@Test
	void gradleBuildWithJpaConfigureHibernateEnhancePlugin() {
		assertThat(gradleBuild(createProjectRequest("native", "data-jpa"))).hasPlugin("org.hibernate.orm").lines()
				.containsSequence(// @formatter:off
				"tasks.named('hibernate') {",
				"	enhance {",
				"		enableLazyInitialization = true",
				"		enableDirtyTracking = true",
				"		enableAssociationManagement = true",
				"		enableExtendedEnhancement = false",
				"	}",
				"}" // @formatter:on
				);
	}

	@Test
	void mavenBuildWithNative0Dot9ConfigureSpringBootPlugin() {
		ProjectRequest request = createProjectRequest("native");
		request.setBootVersion("2.4.7");
		assertThat(mavenPom(request)).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>org.springframework.boot</groupId>",
				"				<artifactId>spring-boot-maven-plugin</artifactId>",
				"				<configuration>",
				"					<image>",
				"						<builder>paketobuildpacks/builder:tiny</builder>",
				"						<env>",
				"							<BP_NATIVE_IMAGE>true</BP_NATIVE_IMAGE>",
				"						</env>",
				"					</image>",
				"				</configuration>",
				"			</plugin>");
		// @formatter:on
	}

	@Test
	void mavenBuildWithNative0Dot10ConfigureSpringBootPlugin() {
		ProjectRequest request = createProjectRequest("native");
		request.setBootVersion("2.5.0");
		assertThat(mavenPom(request)).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>org.springframework.boot</groupId>",
				"				<artifactId>spring-boot-maven-plugin</artifactId>",
				"				<configuration>",
				"					<classifier>${repackage.classifier}</classifier>",
				"					<image>",
				"						<builder>paketobuildpacks/builder:tiny</builder>",
				"						<env>",
				"							<BP_NATIVE_IMAGE>true</BP_NATIVE_IMAGE>",
				"						</env>",
				"					</image>",
				"				</configuration>",
				"			</plugin>");
		// @formatter:on
	}

	@Test
	void mavenBuildWithNative0Dot11ConfigureNativeMavenPlugin() {
		ProjectRequest request = createProjectRequest("native");
		request.setBootVersion("2.6.0-M3");
		assertThat(mavenPom(request)).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>org.springframework.experimental</groupId>",
				"				<artifactId>spring-aot-maven-plugin</artifactId>",
				"				<version>${spring-native.version}</version>",
				"				<executions>",
				"					<execution>",
				"						<id>generate</id>",
				"						<goals>",
				"							<goal>generate</goal>",
				"						</goals>",
				"					</execution>",
				"				</executions>",
				"			</plugin>");
		// @formatter:on
	}

	@Test
	void mavenBuildWithoutJpaDoesNotConfigureHibernateEnhancePlugin() {
		assertThat(mavenPom(createProjectRequest("native"))).doesNotContain("hibernate-enhance-maven-plugin");
	}

	@Test
	void mavenBuildWithJpaConfigureHibernateEnhancePlugin() {
		assertThat(mavenPom(createProjectRequest("native", "data-jpa"))).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>org.hibernate.orm.tooling</groupId>",
				"				<artifactId>hibernate-enhance-maven-plugin</artifactId>",
				"				<version>${hibernate.version}</version>",
				"				<executions>",
				"					<execution>",
				"						<id>enhance</id>",
				"						<goals>",
				"							<goal>enhance</goal>",
				"						</goals>",
				"						<configuration>",
				"							<failOnError>true</failOnError>",
				"							<enableLazyInitialization>true</enableLazyInitialization>",
				"							<enableDirtyTracking>true</enableDirtyTracking>",
				"							<enableAssociationManagement>true</enableAssociationManagement>",
				"							<enableExtendedEnhancement>false</enableExtendedEnhancement>",
				"						</configuration>",
				"					</execution>",
				"				</executions>",
				"			</plugin>");
		// @formatter:on
	}

	private ProjectRequest createNativeProjectRequest(String... dependencies) {
		ProjectRequest projectRequest = createProjectRequest(dependencies);
		projectRequest.getDependencies().add(0, "native");
		projectRequest.setBootVersion("2.6.8");
		return projectRequest;
	}

}
