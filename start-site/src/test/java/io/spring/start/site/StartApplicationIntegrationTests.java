/*
 * Copyright 2012-2019 the original author or authors.
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
import java.util.stream.Collectors;
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
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import io.spring.initializr.web.project.WebProjectRequest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for project generation.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class StartApplicationIntegrationTests {

	private final ProjectGenerationInvoker invoker;

	private final InitializrMetadata metadata;

	StartApplicationIntegrationTests(@Autowired ProjectGenerationInvoker invoker,
			@Autowired InitializrMetadataProvider metadataProvider) {
		this.invoker = invoker;
		this.metadata = metadataProvider.get();
	}

	Stream<Arguments> parameters() {
		List<Version> bootVersions = this.metadata.getBootVersions().getContent().stream()
				.map((element) -> Version.parse(element.getId())).collect(Collectors.toList());
		List<Packaging> packagings = Arrays.asList(new JarPackaging(), new WarPackaging());
		List<Language> languages = Arrays.asList(new KotlinLanguage(), new JavaLanguage());
		List<BuildSystem> buildSystems = Arrays.asList(new GradleBuildSystem(), new MavenBuildSystem());
		List<Arguments> configurations = new ArrayList<>();
		for (Version bootVersion : bootVersions) {
			for (Packaging packaging : packagings) {
				for (Language language : languages) {
					for (BuildSystem buildSystem : buildSystems) {
						configurations.add(Arguments.arguments(bootVersion, packaging, language, buildSystem));
					}
				}
			}
		}
		return configurations.stream();
	}

	@ParameterizedTest(name = "{0} {1} {2} {3}")
	@MethodSource("parameters")
	void projectBuilds(Version bootVersion, Packaging packaging, Language language, BuildSystem buildSystem,
			@TempDir Path directory) throws IOException, InterruptedException {
		WebProjectRequest request = new WebProjectRequest();
		request.setBootVersion(bootVersion.toString());
		request.setLanguage(language.id());
		request.setPackaging(packaging.id());
		request.setType(buildSystem.id() + "-project");
		request.setGroupId("com.example");
		request.setArtifactId("demo");
		request.setApplicationName("DemoApplication");
		request.setDependencies(Arrays.asList("devtools"));
		Path project = this.invoker.invokeProjectStructureGeneration(request).getRootDirectory();
		ProcessBuilder processBuilder = createProcessBuilder(buildSystem);
		processBuilder.directory(project.toFile());
		Path output = Files.createTempFile(directory, "output-", ".log");
		processBuilder.redirectError(output.toFile());
		processBuilder.redirectOutput(output.toFile());
		assertThat(processBuilder.start().waitFor()).describedAs(String.join("\n", Files.readAllLines(output)))
				.isEqualTo(0);
	}

	private ProcessBuilder createProcessBuilder(BuildSystem buildSystem) {
		if (buildSystem.id().equals(new MavenBuildSystem().id())) {
			return new ProcessBuilder("./mvnw", "package");
		}
		if (buildSystem.id().equals(new GradleBuildSystem().id())) {
			return new ProcessBuilder("./gradlew", "--no-daemon", "build");
		}
		throw new IllegalStateException();
	}

}
