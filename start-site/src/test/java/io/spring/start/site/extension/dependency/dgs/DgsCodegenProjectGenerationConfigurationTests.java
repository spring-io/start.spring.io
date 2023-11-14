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

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DgsCodegenProjectGenerationConfiguration}.
 *
 * @author Brian Clozel
 */
class DgsCodegenProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void gradleBuildWithoutDgsCodegenDoesNotConfigureCodegenPlugin() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(gradleBuild(request)).doesNotContain("com.netflix.dgs.codegen");
	}

	@Test
	void mavenBuildWithoutDgsCodegenDoesNotConfigureCodegenPlugin() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).doesNotContain("io.github.deweyjose");
	}

	@Test
	void gradleBuildAddsDgsCodegenPlugin() {
		ProjectRequest projectRequest = createProjectRequest("dgs-codegen");
		assertThat(gradleBuild(projectRequest)).contains("id 'com.netflix.dgs.codegen' version '");
	}

	@Test
	void gradleBuildConfiguresDgsCodegenPlugin() {
		ProjectRequest projectRequest = createProjectRequest("dgs-codegen");
		assertThat(gradleBuild(projectRequest)).containsSubsequence(
		// @formatter:off
				"generateJava {",
				"	schemaPaths = [\"${projectDir}/src/main/resources/graphql-client\"]",
				"	packageName = 'com.example.demo.codegen'",
				"	generateClient = true",
				"}"
				// @formatter:on
		);
	}

	@Test
	void gradleKotlinDslBuildAddsDgsCodegenPlugin() {
		ProjectRequest projectRequest = createProjectRequest("dgs-codegen");
		assertThat(gradleKotlinDslBuild(projectRequest)).contains("id(\"com.netflix.dgs.codegen\") version ");
	}

	@Test
	void gradleKotlinDslBuildConfiguresDgsCodegenPlugin() {
		ProjectRequest projectRequest = createProjectRequest("dgs-codegen");
		assertThat(gradleKotlinDslBuild(projectRequest)).containsSubsequence(
		// @formatter:off
				"tasks.generateJava {",
				"	schemaPaths.add(\"${projectDir}/src/main/resources/graphql-client\")",
				"	packageName = \"com.example.demo.codegen\"",
				"	generateClient = true",
				"}"
				// @formatter:on
		);
	}

	@Test
	void mavenBuildConfiguresCodegenPlugin() {
		ProjectRequest request = createProjectRequest("dgs-codegen");
		assertThat(mavenPom(request)).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>io.github.deweyjose</groupId>",
				"				<artifactId>graphqlcodegen-maven-plugin</artifactId>",
				"				<version>1.50</version>",
				"				<executions>",
				"					<execution>",
				"						<id>dgs-codegen</id>",
				"						<goals>",
				"							<goal>generate</goal>",
				"						</goals>",
				"						<configuration>",
				"							<schemaPaths>",
				"								<param>src/main/resources/graphql-client</param>",
				"							</schemaPaths>",
				"							<packageName>com.example.demo.codegen</packageName>",
				"							<addGeneratedAnnotation>true</addGeneratedAnnotation>",
				"						</configuration>",
				"					</execution>",
				"				</executions>",
				"			</plugin>"
		);
		// @formatter:on
	}

	@Test
	void mavenBuildConfiguresMavenHelperPlugin() {
		ProjectRequest request = createProjectRequest("dgs-codegen");
		assertThat(mavenPom(request)).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>org.codehaus.mojo</groupId>",
				"				<artifactId>build-helper-maven-plugin</artifactId>",
				"				<executions>",
				"					<execution>",
				"						<id>add-dgs-source</id>",
				"						<phase>generate-sources</phase>",
				"						<goals>",
				"							<goal>add-source</goal>",
				"						</goals>",
				"						<configuration>",
				"							<sources>",
				"								<source>${project.build.directory}/generated-sources</source>",
				"							</sources>",
				"						</configuration>",
				"					</execution>",
				"				</executions>",
				"			</plugin>"
		);
		// @formatter:on
	}

}
