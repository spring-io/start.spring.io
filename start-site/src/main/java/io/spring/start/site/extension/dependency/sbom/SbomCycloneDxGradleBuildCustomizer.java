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
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * {@link BuildCustomizer} that adds the CycloneDX Gradle plugin.
 *
 * @author Moritz Halbritter
 */
class SbomCycloneDxGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private static final String PLUGIN_VERSION_BOOT_3_4 = "1.10.0";

	private static final String PLUGIN_VERSION = "2.3.0";

	private static final VersionRange BOOT_3_5_OR_LATER = VersionParser.DEFAULT.parseRange("3.5.0-M1");

	private final ProjectDescription description;

	SbomCycloneDxGradleBuildCustomizer(ProjectDescription description) {
		this.description = description;
	}

	@Override
	public void customize(GradleBuild build) {
		boolean boot35orLater = BOOT_3_5_OR_LATER.match(this.description.getPlatformVersion());
		String pluginVersion = boot35orLater ? PLUGIN_VERSION : PLUGIN_VERSION_BOOT_3_4;
		build.plugins().add("org.cyclonedx.bom", (plugin) -> plugin.setVersion(pluginVersion));
	}

}
