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

import java.util.Collections;
import java.util.List;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * A {@link BuildCustomizer} that automatically excludes annotation processors from the
 * repackaged archive if necessary.
 *
 * @author Stephane Nicoll
 */
class AnnotationProcessorExclusionBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private static final List<String> KNOWN_ANNOTATION_PROCESSORS = Collections
		.singletonList("configuration-processor");

	private final InitializrMetadata metadata;

	AnnotationProcessorExclusionBuildCustomizer(InitializrMetadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public void customize(MavenBuild build) {
		if (!build.plugins().has("org.springframework.boot", "spring-boot-maven-plugin")) {
			return;
		}
		List<io.spring.initializr.generator.buildsystem.Dependency> dependencies = build.dependencies()
			.ids()
			.filter(this::isAnnotationProcessor)
			.filter((id) -> !KNOWN_ANNOTATION_PROCESSORS.contains(id))
			.map((id) -> build.dependencies().get(id))
			.toList();
		if (!dependencies.isEmpty()) {
			build.plugins()
				.add("org.springframework.boot", "spring-boot-maven-plugin", (plugin) -> plugin
					.configuration((configuration) -> configuration.configure("excludes", (excludes) -> {
						for (io.spring.initializr.generator.buildsystem.Dependency dependency : dependencies) {
							excludes.add("exclude", (exclude) -> {
								exclude.add("groupId", dependency.getGroupId());
								exclude.add("artifactId", dependency.getArtifactId());
							});
						}
					})));
		}
	}

	@Override
	public int getOrder() {
		return 5;
	}

	private boolean isAnnotationProcessor(String id) {
		Dependency dependency = this.metadata.getDependencies().get(id);
		return (dependency != null) && Dependency.SCOPE_ANNOTATION_PROCESSOR.equals(dependency.getScope());
	}

}
