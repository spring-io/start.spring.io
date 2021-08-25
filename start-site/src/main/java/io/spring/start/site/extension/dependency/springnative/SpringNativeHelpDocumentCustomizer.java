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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * Provide additional information when Spring Native is selected.
 *
 * @author Stephane Nicoll
 */
class SpringNativeHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final InitializrMetadata metadata;

	private final ProjectDescription description;

	private final Build build;

	private final String springNativeVersion;

	private final String nativeBuildToolsVersion;

	SpringNativeHelpDocumentCustomizer(InitializrMetadata metadata, ProjectDescription description, Build build,
			String springNativeVersion) {
		this.metadata = metadata;
		this.description = description;
		this.build = build;
		this.springNativeVersion = (springNativeVersion != null) ? springNativeVersion : "current";
		this.nativeBuildToolsVersion = (springNativeVersion != null)
				? SpringNativeBuildtoolsVersionResolver.resolve(springNativeVersion) : null;
	}

	@Override
	public void customize(HelpDocument document) {
		BuildSystem buildSystem = this.description.getBuildSystem();
		if (buildSystem instanceof GradleBuildSystem
				&& buildSystem.dialect().equals(GradleBuildSystem.DIALECT_KOTLIN)) {
			handleKotlinDslWarning(document);
		}
		boolean mavenBuild = this.build instanceof MavenBuild;
		String springAotUrl = String.format(
				"https://docs.spring.io/spring-native/docs/%s/reference/htmlsingle/#spring-aot-%s",
				this.springNativeVersion, (mavenBuild) ? "maven" : "gradle");
		document.gettingStarted().addAdditionalLink(springAotUrl, "Configure the Spring AOT Plugin");
		handleUnsupportedDependencies(document);
		Map<String, Object> model = new HashMap<>();
		model.put("version", this.springNativeVersion);
		// Cloud native buildpacks
		model.put("cnbBuildImageCommand", mavenBuild ? "./mvnw spring-boot:build-image" : "./gradlew bootBuildImage");
		model.put("cnbRunImageCommand", createRunImageCommand());
		// Native buildtools plugin
		model.put("nbtBuildImageCommand", mavenBuild ? "./mvnw package -Pnative" : "./gradlew nativeBuild");
		model.put("nbtRunImageCommand", String.format("%s/%s", mavenBuild ? "target" : "build/native-image",
				this.build.getSettings().getArtifact()));

		String templateName = (this.nativeBuildToolsVersion != null) ? "spring-native" : "spring-native-0.9.x";
		document.addSection(templateName, model);
	}

	private void handleKotlinDslWarning(HelpDocument document) {
		String docUrl = String.format(
				"https://docs.spring.io/spring-native/docs/%s/reference/htmlsingle/#_add_the_native_build_tools_plugin",
				this.springNativeVersion);
		String item = String.format(
				"The native build tools is not configured with the Kotlin DSL, check [the documentation](%s) for more details.",
				docUrl);
		document.getWarnings().addItem(item);
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

	private void handleUnsupportedDependencies(HelpDocument document) {
		List<Dependency> unsupportedDependencies = buildDependencies()
				.filter((candidate) -> !candidate.getFacets().contains("native")).collect(Collectors.toList());
		if (!unsupportedDependencies.isEmpty()) {
			StringBuilder sb = new StringBuilder("The following ");
			sb.append((unsupportedDependencies.size() == 1) ? "dependency is " : "dependencies are ");
			sb.append("not known to work with Spring Native: '");
			sb.append(unsupportedDependencies.stream().map(Dependency::getName).collect(Collectors.joining(", ")));
			sb.append("'. As a result, your application may not work as expected.");
			document.getWarnings().addItem(sb.toString());
		}
	}

	private Stream<Dependency> buildDependencies() {
		return this.build.dependencies().ids().map((id) -> this.metadata.getDependencies().get(id))
				.filter(Objects::nonNull);
	}

}
