/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.start.site.extension.dependency.flyway;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Determine the appropriate Flyway dependency according to the database.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 */
class FlywayBuildCustomizer implements BuildCustomizer<Build> {

	private static final VersionRange SPRING_BOOT_2_7_0_OR_LATER = VersionParser.DEFAULT.parseRange("2.7.0");

	private final boolean isSpringBoot27OrLater;

	FlywayBuildCustomizer(ProjectDescription projectDescription) {
		this.isSpringBoot27OrLater = SPRING_BOOT_2_7_0_OR_LATER.match(projectDescription.getPlatformVersion());
	}

	@Override
	public void customize(Build build) {
		if (this.isSpringBoot27OrLater) {
			DependencyContainer dependencies = build.dependencies();
			if ((dependencies.has("mysql") || dependencies.has("mariadb"))) {
				dependencies.add("flyway-mysql", "org.flywaydb", "flyway-mysql", DependencyScope.COMPILE);
			}
			if (dependencies.has("sqlserver")) {
				dependencies.add("flyway-sqlserver", "org.flywaydb", "flyway-sqlserver", DependencyScope.COMPILE);
			}
		}
	}

}
