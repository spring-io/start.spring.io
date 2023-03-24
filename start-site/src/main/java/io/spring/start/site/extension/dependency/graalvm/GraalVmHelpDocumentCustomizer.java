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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * Provide additional information when GraalVM Native Support is selected.
 *
 * @author Stephane Nicoll
 */
class GraalVmHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final InitializrMetadata metadata;

	private final ProjectDescription description;

	private final Build build;

	private final Version platformVersion;

	GraalVmHelpDocumentCustomizer(InitializrMetadata metadata, ProjectDescription description, Build build) {
		this.metadata = metadata;
		this.description = description;
		this.build = build;
		this.platformVersion = description.getPlatformVersion();
	}

	@Override
	public void customize(HelpDocument document) {
		document.gettingStarted()
			.addReferenceDocLink(String.format(
					"https://docs.spring.io/spring-boot/docs/%s/reference/html/native-image.html#native-image",
					this.platformVersion), "GraalVM Native Image Support");
		boolean mavenBuild = this.build instanceof MavenBuild;
		String url = String.format("https://docs.spring.io/spring-boot/docs/%s/%s/reference/htmlsingle/#aot",
				this.platformVersion, (mavenBuild) ? "maven-plugin" : "gradle-plugin");
		document.gettingStarted().addAdditionalLink(url, "Configure AOT settings in Build Plugin");

		Map<String, Object> model = new HashMap<>();
		// Cloud native buildpacks
		model.put("cnbBuildImageCommand",
				mavenBuild ? "./mvnw spring-boot:build-image -Pnative" : "./gradlew bootBuildImage");
		model.put("cnbRunImageCommand", createRunImageCommand());
		// Native buildtools plugin
		model.put("nbtBuildImageCommand", mavenBuild ? "./mvnw native:compile -Pnative" : "./gradlew nativeCompile");
		model.put("nbtRunImageCommand", String.format("%s/%s", mavenBuild ? "target" : "build/native/nativeCompile",
				this.build.getSettings().getArtifact()));
		// Tests execution
		model.put("testsExecutionCommand", mavenBuild ? "./mvnw test -PnativeTest" : "./gradlew nativeTest");

		document.addSection("graalvm", model);
	}

	private String createRunImageCommand() {
		StringBuilder sb = new StringBuilder("docker run --rm");
		boolean hasWeb = buildDependencies().anyMatch((dependency) -> dependency.getFacets().contains("web"));
		if (hasWeb) {
			sb.append(" -p 8080:8080");
		}
		sb.append(" ").append(this.description.getArtifactId()).append(":").append(this.description.getVersion());
		return sb.toString();
	}

	private Stream<Dependency> buildDependencies() {
		return this.build.dependencies()
			.ids()
			.map((id) -> this.metadata.getDependencies().get(id))
			.filter(Objects::nonNull);
	}

}
