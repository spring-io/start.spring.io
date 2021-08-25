/*
 * Copyright 2012-2021 the original author or authors.
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

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringNativeHelpDocumentCustomizer}.
 *
 * @author Stephane Nicoll
 */
class SpringNativeHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Autowired
	private MustacheTemplateRenderer templateRenderer;

	@Test
	void mavenBuildAddLinkToMavenAotPlugin() {
		MutableProjectDescription description = new MutableProjectDescription();
		HelpDocument document = customize(description, new MavenBuild(), "1.0.0");
		assertThat(document.gettingStarted().additionalLinks().getItems()).singleElement().satisfies((link) -> {
			assertThat(link.getDescription()).isEqualTo("Configure the Spring AOT Plugin");
			assertThat(link.getHref()).isEqualTo(
					"https://docs.spring.io/spring-native/docs/1.0.0/reference/htmlsingle/#spring-aot-maven");
		});
	}

	@Test
	void gradleBuildAddLinkToGradleAotPlugin() {
		MutableProjectDescription description = new MutableProjectDescription();
		HelpDocument document = customize(description, new GradleBuild(), "1.5.0");
		assertThat(document.gettingStarted().additionalLinks().getItems()).singleElement().satisfies((link) -> {
			assertThat(link.getDescription()).isEqualTo("Configure the Spring AOT Plugin");
			assertThat(link.getHref()).isEqualTo(
					"https://docs.spring.io/spring-native/docs/1.5.0/reference/htmlsingle/#spring-aot-gradle");
		});
	}

	@Test
	void mavenBuildWithoutSpringNativeVersionAddLinkToCurrent() {
		MutableProjectDescription description = new MutableProjectDescription();
		HelpDocument document = customize(description, new MavenBuild(), null);
		assertThat(document.gettingStarted().additionalLinks().getItems()).singleElement().satisfies((link) -> {
			assertThat(link.getDescription()).isEqualTo("Configure the Spring AOT Plugin");
			assertThat(link.getHref()).isEqualTo(
					"https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#spring-aot-maven");
		});
	}

	private HelpDocument customize(ProjectDescription description, Build build, String springNativeVersion) {
		HelpDocument document = new HelpDocument(this.templateRenderer);
		new SpringNativeHelpDocumentCustomizer(getMetadata(), description, build, springNativeVersion)
				.customize(document);
		return document;
	}

	@Test
	void gradleWithKotlinDslAddsWarning() {
		ProjectRequest request = createProjectRequest("native", "web");
		request.setType("gradle-project");
		request.setLanguage("kotlin");
		assertHelpDocument(request).contains("The native build tools is not configured with the Kotlin DSL");
	}

	@Test
	void gradleWithGroovyDslAddsWarning() {
		ProjectRequest request = createProjectRequest("native", "web");
		request.setType("gradle-project");
		request.setLanguage("java");
		assertHelpDocument(request).doesNotContain("The native build tools is not configured with the Kotlin DSL");
	}

	@Test
	void nativeSectionWithGradleUseGradleCommand() {
		assertHelpDocument("gradle-project", "native").contains("$ ./gradlew bootBuildImage");
	}

	@Test
	void nativeSectionWithMavenUseMavenCommand() {
		assertHelpDocument("maven-project", "native").contains("$ ./mvnw spring-boot:build-image");
	}

	@Test
	void nativeSectionWithoutWebDoesNotExposePort() {
		ProjectRequest request = createProjectRequest("native");
		request.setArtifactId("my-project");
		request.setVersion("1.0.0-SNAPSHOT");
		assertHelpDocument(request).contains("$ docker run --rm my-project:1.0.0-SNAPSHOT");
	}

	@Test
	void nativeSectionWithWebExposesPort() {
		ProjectRequest request = createProjectRequest("native", "web");
		request.setArtifactId("another-project");
		request.setVersion("2.0.0-SNAPSHOT");
		assertHelpDocument(request).contains("$ docker run --rm -p 8080:8080 another-project:2.0.0-SNAPSHOT");
	}

	@Test
	void nativeSectionWithSupportedEntriesDoesNotAddWarning() {
		assertHelpDocument("maven-project", "native", "web")
				.doesNotContain("As a result, your application may not work as expected.");
	}

	@Test
	void nativeSectionWithOneUnsupportedEntryAddWarning() {
		assertHelpDocument("maven-project", "native", "web-services").contains(
				"The following dependency is not known to work with Spring Native: 'Spring Web Services'. As a result, your application may not work as expected.");
	}

	@Test
	void nativeSectionWithSeveralUnsupportedEntriesAddWarning() {
		assertHelpDocument("maven-project", "native", "web-services", "jersey").contains(
				"The following dependencies are not known to work with Spring Native: 'Spring Web Services, Jersey'. As a result, your application may not work as expected.");
	}

	@Test
	void nativeSectionWithNativeBuildtoolsAddsDedicatedSection() {
		ProjectRequest request = createProjectRequest("native");
		request.setBootVersion("2.5.1");
		assertHelpDocument(request).contains("Lightweight Container with Cloud Native Buildpacks",
				"Executable with Native Build Tools");
	}

	@Test
	void nativeSectionWithoutNativeBuildtoolsDoesNotAddDedicatedSection() {
		ProjectRequest request = createProjectRequest("native");
		request.setBootVersion("2.4.5");
		assertHelpDocument(request).doesNotContain("Executable with Native Build Tools");
	}

	private TextAssert assertHelpDocument(ProjectRequest request) {
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md"));
	}

	private TextAssert assertHelpDocument(String type, String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setType(type);
		return assertHelpDocument(request);
	}

}
