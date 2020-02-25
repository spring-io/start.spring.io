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

package io.spring.start.site.extension.dependency.springdata;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.MavenRepository;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} for R2DBC that adds the necessary extra dependencies based on
 * the selected driver.
 *
 * @author Stephane Nicoll
 */
public class R2dbcBuildCustomizer implements BuildCustomizer<Build> {

	private static final MavenRepository SONATYPE_OSS_SNAPSHOTS = MavenRepository
			.withIdAndUrl("sonatype-oss-snapshots", "https://oss.sonatype.org/content/repositories/snapshots")
			.name("Sonatype OSS Snapshots").snapshotsEnabled(true).build();

	@Override
	public void customize(Build build) {
		// Drivers
		if (build.dependencies().has("h2")) {
			build.dependencies().add("r2dbc-h2",
					Dependency.withCoordinates("io.r2dbc", "r2dbc-h2").scope(DependencyScope.RUNTIME));
		}
		if (build.dependencies().has("mysql")) {
			build.dependencies().add("r2dbc-mysql",
					Dependency.withCoordinates("dev.miku", "r2dbc-mysql").scope(DependencyScope.RUNTIME));
			build.repositories().add(SONATYPE_OSS_SNAPSHOTS);
		}
		if (build.dependencies().has("postgresql")) {
			build.dependencies().add("r2dbc-postgresql",
					Dependency.withCoordinates("io.r2dbc", "r2dbc-postgresql").scope(DependencyScope.RUNTIME));
		}
		if (build.dependencies().has("sqlserver")) {
			build.dependencies().add("r2dbc-mssql",
					Dependency.withCoordinates("io.r2dbc", "r2dbc-mssql").scope(DependencyScope.RUNTIME));
		}
		// Actuator
		if (build.dependencies().has("actuator")) {
			build.dependencies().add("r2dbc-actuator-autoconfigure", Dependency.withCoordinates(
					"org.springframework.boot.experimental", "spring-boot-actuator-autoconfigure-r2dbc"));
		}
		// Test specific module
		build.dependencies().add("r2dbc-test-autoconfigure", Dependency
				.withCoordinates("org.springframework.boot.experimental", "spring-boot-test-autoconfigure-r2dbc")
				.scope(DependencyScope.TEST_COMPILE));
	}

}
