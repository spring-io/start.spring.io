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

import java.util.stream.Stream;

import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that annotation processors are never part of the archive repackaged by
 * {@code spring-boot-maven-plugin}, which is why they don't have to be excluded from it
 * explicitly.
 *
 * @author Moritz Halbritter
 */
@TestInstance(Lifecycle.PER_CLASS)
class MavenAnnotationProcessorTests extends AbstractExtensionTests {

	/**
	 * With Java, annotation processors aren't dependencies at all, they are converted to
	 * {@code maven-compiler-plugin} {@code annotationProcessorPaths} entries. Any
	 * dependency left behind on the compile classpath, such as the one Lombok needs, has
	 * to be optional.
	 * @param id the id of the annotation processor dependency
	 * @param artifactId the artifactId of the annotation processor dependency
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("annotationProcessors")
	void javaAnnotationProcessorsAreNotOnTheRuntimeClasspath(String id, String artifactId) {
		ProjectRequest request = createProjectRequest(id);
		request.setLanguage(JavaLanguage.ID);
		assertThat(mavenPom(request)).hasText(annotationProcessorPath(artifactId), artifactId)
			.doesNotHaveNode(nonOptionalDependency(artifactId));
	}

	/**
	 * With Kotlin, annotation processors are regular dependencies, so they have to be
	 * optional as {@code spring-boot-maven-plugin} does not include optional dependencies
	 * in the repackaged archive.
	 * @param id the id of the annotation processor dependency
	 * @param artifactId the artifactId of the annotation processor dependency
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("annotationProcessors")
	void kotlinAnnotationProcessorsAreOptional(String id, String artifactId) {
		ProjectRequest request = createProjectRequest(id);
		request.setLanguage(KotlinLanguage.ID);
		assertThat(mavenPom(request)).hasText(optionalDependency(artifactId), "true")
			.doesNotHaveNode(nonOptionalDependency(artifactId));
	}

	private Stream<Arguments> annotationProcessors() {
		return getMetadata().getDependencies()
			.getAll()
			.stream()
			.filter((dependency) -> Dependency.SCOPE_ANNOTATION_PROCESSOR.equals(dependency.getScope()))
			.map((dependency) -> Arguments.of(dependency.getId(), dependency.getArtifactId()));
	}

	private String annotationProcessorPath(String artifactId) {
		return "/project/build/plugins/plugin[artifactId='maven-compiler-plugin']/executions/execution[id='default-compile']/configuration/annotationProcessorPaths/path[artifactId='%s']/artifactId"
			.formatted(artifactId);
	}

	private String optionalDependency(String artifactId) {
		return "/project/dependencies/dependency[artifactId='%s']/optional".formatted(artifactId);
	}

	private String nonOptionalDependency(String artifactId) {
		return "/project/dependencies/dependency[artifactId='%s' and not(optional='true')]".formatted(artifactId);
	}

}
