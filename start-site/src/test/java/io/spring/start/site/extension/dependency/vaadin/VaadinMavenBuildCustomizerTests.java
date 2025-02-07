/*
 * Copyright 2012-2025 the original author or authors.
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

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link VaadinMavenBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class VaadinMavenBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldAddProductionProfile() {
		ProjectRequest projectRequest = createProjectRequest(SupportedBootVersion.latest(), "vaadin", "web");
		assertThat(mavenPom(projectRequest)).hasProfile("production").lines().containsSequence(
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
        // @formatter:on
	}

}
