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

package io.spring.start.site.extension.dependency.sbom;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.versionresolver.MavenVersionResolver;

/**
 * {@link BuildCustomizer} that adds the CycloneDX Gradle plugin.
 *
 * @author Moritz Halbritter
 */
class SbomCycloneDxGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private static final String DEFAULT_PLUGIN_VERSION = "1.10.0";

	private final MavenVersionResolver versionResolver;

	private final ProjectDescription description;

	SbomCycloneDxGradleBuildCustomizer(MavenVersionResolver versionResolver, ProjectDescription description) {
		this.versionResolver = versionResolver;
		this.description = description;
	}

	@Override
	public void customize(GradleBuild build) {
		String pluginVersion = this.versionResolver
			.resolveDependencies("org.springframework.boot", "spring-boot-parent",
					this.description.getPlatformVersion().toString())
			.get("org.cyclonedx:cyclonedx-gradle-plugin");

		build.plugins()
			.add("org.cyclonedx.bom",
					(plugin) -> plugin.setVersion((pluginVersion != null) ? pluginVersion : DEFAULT_PLUGIN_VERSION));
	}

}
