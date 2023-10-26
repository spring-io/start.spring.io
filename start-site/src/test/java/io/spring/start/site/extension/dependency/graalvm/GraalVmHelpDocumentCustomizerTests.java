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

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GraalVmHelpDocumentCustomizer}.
 *
 * @author Stephane Nicoll
 */
class GraalVmHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Autowired
	private MustacheTemplateRenderer templateRenderer;

	@Test
	void mavenBuildAddLinkToMavenAotPlugin() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setPlatformVersion(Version.parse("3.0.0-RC1"));
		HelpDocument document = customize(description, new MavenBuild());
		assertThat(document.gettingStarted().additionalLinks().getItems()).singleElement().satisfies((link) -> {
			assertThat(link.getDescription()).isEqualTo("Configure AOT settings in Build Plugin");
			assertThat(link.getHref())
				.isEqualTo("https://docs.spring.io/spring-boot/docs/3.0.0-RC1/maven-plugin/reference/htmlsingle/#aot");
		});
	}

	@Test
	void gradleBuildAddLinkToGradleAotPlugin() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setPlatformVersion(Version.parse("3.0.0-RC1"));
		HelpDocument document = customize(description, new GradleBuild());
		assertThat(document.gettingStarted().additionalLinks().getItems()).singleElement().satisfies((link) -> {
			assertThat(link.getDescription()).isEqualTo("Configure AOT settings in Build Plugin");
			assertThat(link.getHref())
				.isEqualTo("https://docs.spring.io/spring-boot/docs/3.0.0-RC1/gradle-plugin/reference/htmlsingle/#aot");
		});
	}

	private HelpDocument customize(ProjectDescription description, Build build) {
		HelpDocument document = new HelpDocument(this.templateRenderer);
		new GraalVmHelpDocumentCustomizer(getMetadata(), description, build).customize(document);
		return document;
	}

	@Test
	void nativeSectionWithGradleUseGradleCommand() {
		assertHelpDocument("gradle-project", "native").contains("$ ./gradlew bootBuildImage")
			.contains("$ ./gradlew nativeCompile");
	}

	@Test
	void nativeSectionWithMavenUseMavenCommand() {
		assertHelpDocument("maven-project", "native").contains("$ ./mvnw spring-boot:build-image -Pnative")
			.contains("$ ./mvnw native:compile -Pnative");
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
