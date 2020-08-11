/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.start.site.extension.dependency.springboot;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradleDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Gradle {@link BuildCustomizer} that creates a dedicated "developmentOnly" configuration
 * when devtools is selected.
 *
 * @author Stephane Nicoll
 */
class DevToolsGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private static final VersionRange SPRING_BOOT_2_3_0_RC1_OR_LATER = VersionParser.DEFAULT.parseRange("2.3.0.RC1");

	private final ProjectDescription projectDescription;

	DevToolsGradleBuildCustomizer(ProjectDescription projectDescription) {
		this.projectDescription = projectDescription;
	}

	@Override
	public void customize(GradleBuild build) {
		if (!SPRING_BOOT_2_3_0_RC1_OR_LATER.match(this.projectDescription.getPlatformVersion())) {
			build.configurations().add("developmentOnly");
			build.configurations().customize("runtimeClasspath",
					(runtimeClasspath) -> runtimeClasspath.extendsFrom("developmentOnly"));
		}
		Dependency devtools = build.dependencies().get("devtools");
		build.dependencies().add("devtools", GradleDependency.from(devtools).configuration("developmentOnly"));
	}

}
