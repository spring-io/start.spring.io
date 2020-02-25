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

package io.spring.start.site.extension.code.kotlin;

import java.util.Map;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildMetadataResolver;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;

/**
 * A project customizer for Kotlin Coroutines.
 *
 * @author Stephane Nicoll
 */
public class KotlinCoroutinesCustomizer {

	private final BuildMetadataResolver buildResolver;

	private final ProjectDescription description;

	private final DependencyManagementVersionResolver versionResolver;

	public KotlinCoroutinesCustomizer(InitializrMetadata metadata, ProjectDescription description,
			DependencyManagementVersionResolver versionResolver) {
		this.buildResolver = new BuildMetadataResolver(metadata);
		this.description = description;
		this.versionResolver = versionResolver;
	}

	public void customize(Build build) {
		if (hasReactiveFacet(build)) {
			build.dependencies().add("kotlinx-coroutines-reactor",
					Dependency.withCoordinates("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor"));
		}
	}

	public void customize(HelpDocument document, Build build) {
		if (hasReactiveFacet(build)) {
			Map<String, String> resolve = this.versionResolver.resolve("org.springframework.boot",
					"spring-boot-dependencies", this.description.getPlatformVersion().toString());
			String frameworkVersion = resolve.get("org.springframework:spring-core");
			String versionToUse = (frameworkVersion != null) ? frameworkVersion : "current";
			String href = String.format(
					"https://docs.spring.io/spring/docs/%s/spring-framework-reference/languages.html#coroutines",
					versionToUse);
			document.gettingStarted().addReferenceDocLink(href,
					"Coroutines section of the Spring Framework Documentation");
		}
	}

	private boolean hasReactiveFacet(Build build) {
		return this.buildResolver.hasFacet(build, "reactive");
	}

}
