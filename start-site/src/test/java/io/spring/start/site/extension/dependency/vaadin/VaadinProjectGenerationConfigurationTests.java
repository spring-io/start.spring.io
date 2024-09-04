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

package io.spring.start.site.extension.dependency.vaadin;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.web.project.ProjectRequest;
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

	private static final String SPRING_BOOT_VERSION = "3.3.0";

	@Test
	void mavenBuildWithVaadinAddProductionProfileWithoutProductionModeFlag() {
		ProjectRequest request = createProjectRequest("vaadin", "data-jpa");
		request.setBootVersion(SPRING_BOOT_VERSION);
		assertThat(mavenPom(request)).hasProfile("production").lines().containsSequence(
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
	void mavenBuildWithoutVaadinDoesNotAddProductionProfile() {
		assertThat(mavenPom(createProjectRequest("data-jpa"))).doesNotHaveProfile("production");
	}

	@Test
	void gradleBuildWithVaadinAddPlugin() {
		ProjectRequest request = createProjectRequest("vaadin", "data-jpa");
		String vaadinVersion = getMetadata().getConfiguration()
			.getEnv()
			.getBoms()
			.get("vaadin")
			.resolve(Version.parse(request.getBootVersion()))
			.getVersion();
		assertThat(gradleBuild(request)).hasPlugin("com.vaadin", vaadinVersion);
	}

	@Test
	void gradleBuildWithoutVaadinDoesNotAddPlugin() {
		assertThat(gradleBuild(createProjectRequest("data-jpa"))).doesNotContain("id 'com.vaadin'");
	}

	@Test
	void gitIgnoreWithVaadinIgnoreNodeModules() {
		assertThat(generateProject(createProjectRequest("vaadin", "data-jpa"))).textFile(".gitignore")
			.contains("node_modules");
	}

	@Test
	void gitIgnoreWithoutVaadinDoesNotIgnoreNodeModules() {
		assertThat(generateProject(createProjectRequest("data-jpa"))).textFile(".gitignore")
			.doesNotContain("node_modules");
	}

	@Test
	void shouldAddLaunchBrowserProperty() {
		assertThat(generateProject(createProjectRequest("vaadin"))).textFile("src/main/resources/application.properties")
				.contains("vaadin.launch-browser=true");
	}

	@Test
	void shouldNotAddLaunchBrowserPropertyIfVaadinIsNotSelected() {
		assertThat(generateProject(createProjectRequest("data-jpa"))).textFile("src/main/resources/application.properties")
				.doesNotContain("vaadin.launch-browser=true");
	}

	@Override
	protected ProjectRequest createProjectRequest(String... dependencies) {
		ProjectRequest request = super.createProjectRequest(dependencies);
		request.setBootVersion(SPRING_BOOT_VERSION);
		return request;
	}

}
