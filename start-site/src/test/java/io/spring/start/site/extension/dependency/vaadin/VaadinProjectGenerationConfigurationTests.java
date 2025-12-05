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

package io.spring.start.site.extension.dependency.vaadin;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link VaadinProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class VaadinProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V3_5;

	@Test
	void mavenBuildWithVaadinAddProductionProfileWithoutProductionModeFlagOnBoot35() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "vaadin", "data-jpa");
		assertThat(mavenPom(request)).hasProfile("production")
			.doesNotHaveDependency("com.vaadin", "vaadin-dev")
			.lines()
			.containsSequence(
			// @formatter:off
				"		<profile>",
				"			<id>production</id>",
				"			<dependencies>",
				"				<dependency>",
				"					<groupId>com.vaadin</groupId>",
				"					<artifactId>vaadin-core</artifactId>",
				"					<exclusions>",
				"						<exclusion>",
				"							<groupId>com.vaadin</groupId>",
				"							<artifactId>vaadin-dev</artifactId>",
				"						</exclusion>",
				"					</exclusions>",
				"				</dependency>",
				"",
				"			</dependencies>",
				"			<build>",
				"				<plugins>",
				"					<plugin>",
				"						<groupId>com.vaadin</groupId>",
				"						<artifactId>vaadin-maven-plugin</artifactId>",
				"						<version>${vaadin.version}</version>",
				"						<executions>",
				"							<execution>",
				"								<id>frontend</id>",
				"								<phase>compile</phase>",
				"								<goals>",
				"									<goal>prepare-frontend</goal>",
				"									<goal>build-frontend</goal>",
				"								</goals>",
				"							</execution>",
				"						</executions>",
				"					</plugin>",
				"				</plugins>",
				"			</build>",
				"		</profile>"
		);
	}

	@Test
	void mavenBuildWithVaadinAddsVaadinDevDependencyWithBoot4() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "vaadin", "data-jpa");
		assertThat(mavenPom(request)).doesNotHaveProfile("production")
				.containsIgnoringWhitespaces(
					"""
					<dependency>
						<groupId>com.vaadin</groupId>
						<artifactId>vaadin-dev</artifactId>
						<optional>true</optional>
					</dependency>
					""")
				.containsIgnoringWhitespaces(
					"""
					<plugin>
						<groupId>com.vaadin</groupId>
						<artifactId>vaadin-maven-plugin</artifactId>
						<version>${vaadin.version}</version>
						<executions>
							<execution>
								<id>build-frontend</id>
								<goals>
									<goal>build-frontend</goal>
								</goals>
							</execution>
						</executions>
					</plugin>
					""");
	}

	@Test
	void mavenBuildWithoutVaadinDoesNotAddProductionProfile() {
		assertThat(mavenPom(createProjectRequest("data-jpa"))).doesNotHaveProfile("production");
	}

	@Test
	void gradleBuildWithVaadinAddPlugin() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "vaadin", "data-jpa");
		String vaadinVersion = getMetadata().getConfiguration()
			.getEnv()
			.getBoms()
			.get("vaadin")
			.resolve(Version.parse(request.getBootVersion()))
			.getVersion();
		assertThat(gradleBuild(request)).hasPlugin("com.vaadin", vaadinVersion)
				.doesNotContain("developmentOnly 'com.vaadin:vaadin-dev'");
	}

	@Test
	void gradleBuildWithVaadinAddsVaadinDevDependencyWithBoot4() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "vaadin", "data-jpa");
		assertThat(gradleBuild(request))
				.contains("developmentOnly 'com.vaadin:vaadin-dev'");
	}

	@Test
	void gradleBuildWithoutVaadinDoesNotAddPlugin() {
		assertThat(gradleBuild(createProjectRequest("data-jpa"))).doesNotContain("id 'com.vaadin'");
	}

	@Test
	void gitIgnoreWithVaadinIgnoreNodeModules() {
		assertThat(generateProject(createProjectRequest(BOOT_VERSION, "vaadin", "data-jpa"))).textFile(".gitignore")
			.contains("node_modules");
	}

	@Test
	void gitIgnoreWithoutVaadinDoesNotIgnoreNodeModules() {
		assertThat(generateProject(createProjectRequest("data-jpa"))).textFile(".gitignore")
			.doesNotContain("node_modules");
	}

	@Test
	void shouldAddLaunchBrowserProperty() {
		assertThat(applicationProperties(createProjectRequest(BOOT_VERSION, "vaadin"))).lines().contains("vaadin.launch-browser=true");
	}

	@Test
	void shouldNotAddLaunchBrowserPropertyIfVaadinIsNotSelected() {
		assertThat(applicationProperties(createProjectRequest("data-jpa"))).lines().doesNotContain("vaadin.launch-browser=true");
	}

}
