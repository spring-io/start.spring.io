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

package io.spring.start.site;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.packaging.Packaging;
import io.spring.initializr.generator.packaging.jar.JarPackaging;
import io.spring.initializr.generator.packaging.war.WarPackaging;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.DefaultMetadataElement;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.metadata.MetadataElement;
import io.spring.initializr.web.project.DefaultProjectRequestToDescriptionConverter;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.initializr.web.project.WebProjectRequest;
import io.spring.start.testsupport.Homes;
import io.spring.start.testsupport.TemporaryFiles;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for project generation.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class ProjectGenerationIntegrationTests {

	private final ProjectGenerationInvoker<ProjectRequest> invoker;

	private final InitializrMetadata metadata;

	ProjectGenerationIntegrationTests(@Autowired ApplicationContext applicationContext,
			@Autowired InitializrMetadataProvider metadataProvider) {
		this.invoker = new ProjectGenerationInvoker<>(applicationContext,
				new DefaultProjectRequestToDescriptionConverter());
		this.metadata = metadataProvider.get();
	}

	Stream<Arguments> parameters() {
		List<Version> bootVersions = this.metadata.getBootVersions()
			.getContent()
			.stream()
			.map((element) -> Version.parse(element.getId()))
			.toList();
		String defaultJvmVersion = this.metadata.getJavaVersions()
			.getContent()
			.stream()
			.filter(DefaultMetadataElement::isDefault)
			.map(MetadataElement::getId)
			.findAny()
			.orElse("11");
		List<Packaging> packagings = Arrays.asList(new JarPackaging(), new WarPackaging());
		List<Language> languages = Arrays.asList(Language.forId(KotlinLanguage.ID, defaultJvmVersion),
				Language.forId(JavaLanguage.ID, defaultJvmVersion));
		BuildSystem maven = BuildSystem.forId(MavenBuildSystem.ID);
		BuildSystem gradleGroovy = BuildSystem.forIdAndDialect(GradleBuildSystem.ID, GradleBuildSystem.DIALECT_GROOVY);
		BuildSystem gradleKotlin = BuildSystem.forIdAndDialect(GradleBuildSystem.ID, GradleBuildSystem.DIALECT_KOTLIN);
		List<Arguments> configurations = new ArrayList<>();
		for (Version bootVersion : bootVersions) {
			for (Packaging packaging : packagings) {
				for (Language language : languages) {
					configurations.add(Arguments.arguments(bootVersion, packaging, language, maven));
					configurations.add(Arguments.arguments(bootVersion, packaging, language,
							(language.id().equals(KotlinLanguage.ID)) ? gradleKotlin : gradleGroovy));
				}
			}
		}
		return configurations.stream();
	}

	@ParameterizedTest(name = "{0} - {1} - {2} - {3}")
	@MethodSource("parameters")
	void projectBuilds(Version bootVersion, Packaging packaging, Language language, BuildSystem buildSystem)
			throws IOException, InterruptedException {
		WebProjectRequest request = new WebProjectRequest();
		request.setBootVersion(bootVersion.toString());
		request.setLanguage(language.id());
		request.setPackaging(packaging.id());
		request.setType(buildSystem.id() + "-project");
		request.setGroupId("com.example");
		request.setArtifactId("demo");
		request.setApplicationName("DemoApplication");
		request.setDependencies(Arrays.asList("devtools", "configuration-processor"));
		Path project = this.invoker.invokeProjectStructureGeneration(request).getRootDirectory();
		Path home = getHome(buildSystem);
		ProcessBuilder processBuilder = createProcessBuilder(project, buildSystem, home);
		Path output = TemporaryFiles.newTemporaryDirectory("ProjectGenerationIntegrationTests-projectBuilds")
			.resolve("output.log");
		processBuilder.redirectError(output.toFile());
		processBuilder.redirectOutput(output.toFile());
		assertThat(processBuilder.start().waitFor()).describedAs(String.join("\n", Files.readAllLines(output)))
			.isEqualTo(0);
	}

	private Path getHome(BuildSystem buildSystem) {
		return switch (buildSystem.id()) {
			case MavenBuildSystem.ID -> Homes.MAVEN.get();
			case GradleBuildSystem.ID -> Homes.GRADLE.get();
			default -> throw new IllegalStateException("Unknown build system '%s'".formatted(buildSystem.id()));
		};
	}

	private ProcessBuilder createProcessBuilder(Path directory, BuildSystem buildSystem, Path home) {
		if (buildSystem.id().equals(MavenBuildSystem.ID)) {
			return createMavenProcessBuilder(directory, home);
		}
		if (buildSystem.id().equals(GradleBuildSystem.ID)) {
			return createGradleProcessBuilder(directory, home);
		}
		throw new IllegalStateException("Unknown build system '%s'".formatted(buildSystem.id()));
	}

	private ProcessBuilder createGradleProcessBuilder(Path directory, Path home) {
		String command = (isWindows()) ? "gradlew.bat" : "gradlew";
		ProcessBuilder processBuilder = new ProcessBuilder(directory.resolve(command).toAbsolutePath().toString(),
				"--no-daemon", "build");
		processBuilder.environment().put("GRADLE_USER_HOME", home.toAbsolutePath().toString());
		processBuilder.directory(directory.toFile());
		return processBuilder;
	}

	private ProcessBuilder createMavenProcessBuilder(Path directory, Path home) {
		String command = (isWindows()) ? "mvnw.cmd" : "mvnw";
		ProcessBuilder processBuilder = new ProcessBuilder(directory.resolve(command).toAbsolutePath().toString(),
				"-Dmaven.repo.local=" + home.resolve("repository").toAbsolutePath(), "package");
		processBuilder.environment().put("MAVEN_USER_HOME", home.toAbsolutePath().toString());
		processBuilder.directory(directory.toFile());
		return processBuilder;
	}

	private boolean isWindows() {
		return SystemUtils.IS_OS_WINDOWS;
	}

}
