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

import java.util.ArrayList;
import java.util.List;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * {@link BuildCustomizer} that adds the CycloneDX Gradle plugin.
 *
 * @author Moritz Halbritter
 */
class SbomCycloneDxGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final ProjectDescription description;

	private final PluginVersionMapping pluginVersionMapping;

	SbomCycloneDxGradleBuildCustomizer(ProjectDescription description) {
		this.description = description;
		this.pluginVersionMapping = new PluginVersionMapping("3.0.1");
		this.pluginVersionMapping.addVersion("[1.0.0,3.5.0-M1)", "1.10.0");
		this.pluginVersionMapping.addVersion("[3.5.0-M1,4.0.0-RC1)", "2.3.0");
	}

	@Override
	public void customize(GradleBuild build) {
		String pluginVersion = this.pluginVersionMapping.getPluginVersion(this.description.getPlatformVersion());
		build.plugins().add("org.cyclonedx.bom", (plugin) -> plugin.setVersion(pluginVersion));
	}

	private static final class PluginVersionMapping {

		private final String defaultVersion;

		private final List<Mapping> mappings = new ArrayList<>();

		PluginVersionMapping(String defaultVersion) {
			this.defaultVersion = defaultVersion;
		}

		void addVersion(String bootVersion, String pluginVersion) {
			this.mappings.add(new Mapping(VersionParser.DEFAULT.parseRange(bootVersion), pluginVersion));
		}

		String getPluginVersion(Version bootVersion) {
			for (Mapping mapping : this.mappings) {
				if (mapping.bootVersion().match(bootVersion)) {
					return mapping.pluginVersion();
				}
			}
			return this.defaultVersion;
		}

		private record Mapping(VersionRange bootVersion, String pluginVersion) {
		}

	}

}
