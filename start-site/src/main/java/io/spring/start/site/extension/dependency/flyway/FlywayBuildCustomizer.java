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
 * @author Moritz Halbritter
 */
class FlywayBuildCustomizer implements BuildCustomizer<Build> {

	private static final VersionRange SPRING_BOOT_3_2_M1_OR_LATER = VersionParser.DEFAULT.parseRange("3.2.0-M1");

	private static final VersionRange SPRING_BOOT_3_3_M3_OR_LATER = VersionParser.DEFAULT.parseRange("3.3.0-M3");

	private final boolean isSpringBoot32OrLater;

	private final boolean isSpringBoot33OrLater;

	FlywayBuildCustomizer(ProjectDescription projectDescription) {
		this.isSpringBoot32OrLater = SPRING_BOOT_3_2_M1_OR_LATER.match(projectDescription.getPlatformVersion());
		this.isSpringBoot33OrLater = SPRING_BOOT_3_3_M3_OR_LATER.match(projectDescription.getPlatformVersion());
	}

	@Override
	public void customize(Build build) {
		DependencyContainer dependencies = build.dependencies();
		if ((dependencies.has("mysql") || dependencies.has("mariadb"))) {
			dependencies.add("flyway-mysql", "org.flywaydb", "flyway-mysql", DependencyScope.COMPILE);
		}
		if (dependencies.has("sqlserver")) {
			dependencies.add("flyway-sqlserver", "org.flywaydb", "flyway-sqlserver", DependencyScope.COMPILE);
		}
		if (this.isSpringBoot32OrLater) {
			if (dependencies.has("oracle")) {
				dependencies.add("flyway-oracle", "org.flywaydb", "flyway-database-oracle", DependencyScope.COMPILE);
			}
		}
		if (this.isSpringBoot33OrLater) {
			if (dependencies.has("db2")) {
				dependencies.add("flyway-database-db2", "org.flywaydb", "flyway-database-db2", DependencyScope.COMPILE);
			}
			if (dependencies.has("derby")) {
				dependencies.add("flyway-database-derby", "org.flywaydb", "flyway-database-derby",
						DependencyScope.COMPILE);
			}
			if (dependencies.has("hsql")) {
				dependencies.add("flyway-database-hsqldb", "org.flywaydb", "flyway-database-hsqldb",
						DependencyScope.COMPILE);
			}
			if (dependencies.has("postgresql")) {
				dependencies.add("flyway-database-postgresql", "org.flywaydb", "flyway-database-postgresql",
						DependencyScope.COMPILE);
			}
		}
	}

}
