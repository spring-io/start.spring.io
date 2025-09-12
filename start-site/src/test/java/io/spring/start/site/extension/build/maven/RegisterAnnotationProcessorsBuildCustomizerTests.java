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

package io.spring.start.site.extension.build.maven;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RegisterAnnotationProcessorsBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class RegisterAnnotationProcessorsBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldNotAddCompilerPluginWhenNoAnnotationProcessorsAreSelected() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).doesNotContain("<artifactId>maven-compiler-plugin</artifactId>");
	}

	@Test
	void shouldRegisterLombok() {
		ProjectRequest request = createProjectRequest("lombok");
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-compiler-plugin</artifactId>
					<configuration>
						<annotationProcessorPaths>
							<path>
								<groupId>org.projectlombok</groupId>
								<artifactId>lombok</artifactId>
							</path>
						</annotationProcessorPaths>
					</configuration>
				</plugin>""");
	}

	@Test
	void shouldRegisterSpringBootConfigurationProcessor() {
		ProjectRequest request = createProjectRequest("configuration-processor");
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-compiler-plugin</artifactId>
					<configuration>
						<annotationProcessorPaths>
							<path>
								<groupId>org.springframework.boot</groupId>
								<artifactId>spring-boot-configuration-processor</artifactId>
							</path>
						</annotationProcessorPaths>
					</configuration>
				</plugin>""");
	}

	@Test
	void shouldRegisterMultiple() {
		ProjectRequest request = createProjectRequest("lombok", "configuration-processor");
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-compiler-plugin</artifactId>
					<configuration>
						<annotationProcessorPaths>
							<path>
								<groupId>org.projectlombok</groupId>
								<artifactId>lombok</artifactId>
							</path>
							<path>
								<groupId>org.springframework.boot</groupId>
								<artifactId>spring-boot-configuration-processor</artifactId>
							</path>
						</annotationProcessorPaths>
					</configuration>
				</plugin>""");
	}

	@ParameterizedTest
	@ValueSource(strings = { "groovy", "kotlin" })
	void shouldDoNothingIfLanguageIsNotJava(String language) {
		ProjectRequest request = createProjectRequest("configuration-processor");
		request.setLanguage(language);
		assertThat(mavenPom(request)).doesNotContain("<artifactId>maven-compiler-plugin</artifactId>");
	}

}
