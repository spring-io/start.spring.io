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

import java.util.List;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * Register annotation processor dependencies on the Maven compiler plugin.
 *
 * @author Moritz Halbritter
 */
class RegisterAnnotationProcessorsBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private final InitializrMetadata metadata;

	private final ProjectDescription projectDescription;

	RegisterAnnotationProcessorsBuildCustomizer(InitializrMetadata metadata, ProjectDescription projectDescription) {
		this.metadata = metadata;
		this.projectDescription = projectDescription;
	}

	@Override
	public void customize(MavenBuild build) {
		if (!isJava()) {
			return;
		}
		List<Dependency> annotationProcessors = build.dependencies()
			.ids()
			.filter(this::isAnnotationProcessor)
			.map((id) -> build.dependencies().get(id))
			.toList();
		if (annotationProcessors.isEmpty()) {
			return;
		}
		build.plugins().add("org.apache.maven.plugins", "maven-compiler-plugin", (plugin) -> {
			plugin.configuration((configuration) -> {
				configuration.add("annotationProcessorPaths", (annotationProcessorPaths) -> {
					for (Dependency annotationProcessor : annotationProcessors) {
						annotationProcessorPaths.add("path", (path) -> {
							path.add("groupId", annotationProcessor.getGroupId());
							path.add("artifactId", annotationProcessor.getArtifactId());
						});

					}
				});
			});
		});

	}

	private boolean isAnnotationProcessor(String id) {
		io.spring.initializr.metadata.Dependency dependency = this.metadata.getDependencies().get(id);
		return (dependency != null)
				&& io.spring.initializr.metadata.Dependency.SCOPE_ANNOTATION_PROCESSOR.equals(dependency.getScope());
	}

	private boolean isJava() {
		return this.projectDescription.getLanguage().id().equals(JavaLanguage.ID);
	}

}
