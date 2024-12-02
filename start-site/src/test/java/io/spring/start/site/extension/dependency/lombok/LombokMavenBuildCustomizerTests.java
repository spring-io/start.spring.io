/*
 * Copyright 2012-2024 the original author or authors.
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

package io.spring.start.site.extension.dependency.lombok;

import io.spring.initializr.generator.language.groovy.GroovyLanguage;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LombokMavenBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class LombokMavenBuildCustomizerTests extends AbstractExtensionTests {

	@ParameterizedTest
	@ValueSource(ints = { 23, 24 })
	void shouldAddAnnotationProcessorsOnJava23AndUp(int javaVersion) {
		ProjectRequest request = createProjectRequest("lombok");
		request.setJavaVersion(Integer.toString(javaVersion));
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

	@ParameterizedTest
	@ValueSource(ints = { 17, 18, 19, 20, 21, 22 })
	void shouldNotAddAnnotationProcessorsOnJavaBelow23(int javaVersion) {
		ProjectRequest request = createProjectRequest("lombok");
		request.setJavaVersion(Integer.toString(javaVersion));
		assertThat(mavenPom(request)).doesNotContain("<annotationProcessorPaths>");
	}

	@ParameterizedTest
	@ValueSource(strings = { GroovyLanguage.ID, KotlinLanguage.ID })
	void shouldNotAddAnnotationProcessorsOnNonJavaProjects(String language) {
		ProjectRequest request = createProjectRequest("lombok");
		request.setJavaVersion("23");
		request.setLanguage(language);
		assertThat(mavenPom(request)).doesNotContain("<annotationProcessorPaths>");
	}

}
