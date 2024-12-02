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

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * {@link BuildCustomizer} for Lombok when using Maven.
 *
 * @author Moritz Halbritter
 */
public class LombokMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private final InitializrMetadata metadata;

	private final ProjectDescription projectDescription;

	public LombokMavenBuildCustomizer(InitializrMetadata metadata, ProjectDescription projectDescription) {
		this.metadata = metadata;
		this.projectDescription = projectDescription;
	}

	@Override
	public void customize(MavenBuild build) {
		if (isAtLeastJava(23)) {
			configureAnnotationProcessor(build);
		}
	}

	private void configureAnnotationProcessor(MavenBuild build) {
		Dependency lombok = this.metadata.getDependencies().get("lombok");
		build.plugins().add("org.apache.maven.plugins", "maven-compiler-plugin", (plugin) -> {
			plugin.configuration((configuration) -> {
				configuration.add("annotationProcessorPaths", (annotationProcessorPaths) -> {
					annotationProcessorPaths.add("path", (path) -> {
						path.add("groupId", lombok.getGroupId());
						path.add("artifactId", lombok.getArtifactId());
					});
				});
			});
		});
	}

	private boolean isAtLeastJava(int version) {
		if (!isJava()) {
			return false;
		}
		return getJavaVersion() >= version;
	}

	private int getJavaVersion() {
		String javaVersion = this.projectDescription.getLanguage().jvmVersion();
		try {
			return Integer.parseInt(javaVersion);
		}
		catch (NumberFormatException ex) {
			return -1;
		}
	}

	private boolean isJava() {
		return this.projectDescription.getLanguage().id().equals(JavaLanguage.ID);
	}

}
