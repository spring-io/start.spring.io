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

package io.spring.start.site.extension;

import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.version.Version;

/**
 * A {@link ProjectDescriptionCustomizer} that enables the Kotlin DSL for a Gradle project
 * using Kotlin.
 *
 * @author Stephane Nicoll
 */
public class GradleDslProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private static final Version VERSION_2_1_0_M1 = Version.parse("2.1.0.M1");

	@Override
	public void customize(ProjectDescription description) {
		if (isCompatibleSpringBootVersion(description) && description.getLanguage() instanceof KotlinLanguage
				&& description.getBuildSystem() instanceof GradleBuildSystem) {
			description.setBuildSystem(
					BuildSystem.forIdAndDialect(GradleBuildSystem.ID, GradleBuildSystem.DIALECT_KOTLIN));
		}
	}

	protected boolean isCompatibleSpringBootVersion(ProjectDescription description) {
		return VERSION_2_1_0_M1.compareTo(description.getPlatformVersion()) <= 0;
	}

}
