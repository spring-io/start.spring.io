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

package io.spring.start.site.extension.dependency.jte;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JteProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class JteProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private final Dependency jte = Dependency.withId("jte", "gg.jte", "jte-spring-boot-starter-3");

	@Test
	void shouldDoNothingIfJteIsNotSelected() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).doesNotHaveDependency(this.jte.getGroupId(), this.jte.getArtifactId());
		assertThat(mavenPom(request)).doesNotHaveDependency("gg.jte", "jte");
		ProjectStructure structure = generateProject(request);
		assertThat(structure).doesNotContainDirectories("src/main/jte");
		assertThat(applicationProperties(request)).lines().doesNotContain("gg.jte.development-mode=true");
		assertThat(gitIgnore(request)).lines().doesNotContain("/jte-classes/");
		assertThat(helpDocument(request)).lines().doesNotContain("## JTE");
	}

	@Test
	void shouldCreateTemplateDirectory() {
		ProjectRequest request = createProjectRequest("jte");
		ProjectStructure structure = generateProject(request);
		assertThat(structure).containsDirectories("src/main/jte");
	}

	@Test
	void shouldAddProperties() {
		ProjectRequest request = createProjectRequest("jte");
		assertThat(applicationProperties(request)).lines().contains("gg.jte.development-mode=true");
	}

	@Test
	void shouldAddGitIgnoreEntries() {
		ProjectRequest request = createProjectRequest("jte");
		assertThat(gitIgnore(request)).lines().contains("/jte-classes/");
	}

	@Test
	void shouldAddHelpSection() {
		ProjectRequest request = createProjectRequest("jte");
		assertThat(helpDocument(request)).lines().contains("## JTE");
	}

	@Test
	void shouldAddGradlePluginAndConfigureIt() {
		String jteVersion = getDependency("jte").getVersion();
		ProjectRequest request = createProjectRequest("jte");
		assertThat(gradleBuild(request)).hasPlugin("gg.jte.gradle", jteVersion).containsIgnoringWhitespaces("""
				jte {
					generate()
					binaryStaticContent = true
				}
				""");
	}

	@Test
	void shouldAddMavenPluginAndConfigureIt() {
		String jteVersion = getDependency("jte").getVersion();
		ProjectRequest request = createProjectRequest("jte");
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
				<plugin>
					<groupId>gg.jte</groupId>
					<artifactId>jte-maven-plugin</artifactId>
					<version>%s</version>
					<executions>
						<execution>
							<id>jte-generate</id>
							<phase>generate-sources</phase>
							<goals>
								<goal>generate</goal>
							</goals>
							<configuration>
								<sourceDirectory>${project.basedir}/src/main/jte</sourceDirectory>
								<contentType>Html</contentType>
								<binaryStaticContent>true</binaryStaticContent>
				    			<targetResourceDirectory>${project.build.outputDirectory}</targetResourceDirectory>
							</configuration>
						</execution>
					</executions>
				</plugin>
				""".formatted(jteVersion));
	}

}
