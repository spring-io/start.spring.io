/*
 * Copyright 2012-2023 the original author or authors.
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

import java.util.Arrays;
import java.util.List;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.Dependency.Builder;
import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionReference;

/**
 * A {@link BuildCustomizer} for R2DBC that adds the necessary extra dependencies based on
 * the selected driver, and make sure that {@code spring-jdbc} is available if Flyway or
 * Liquibase is selected.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @author Brian Clozel
 * @author Eddú Meléndez
 */
public class R2dbcBuildCustomizer implements BuildCustomizer<Build> {

	private static final List<String> JDBC_DEPENDENCY_IDS = Arrays.asList("jdbc", "data-jdbc", "data-jpa");

	@Override
	public void customize(Build build) {
		if (build.dependencies().has("h2")) {
			addManagedDriver(build.dependencies(), "io.r2dbc", "r2dbc-h2");
		}
		if (build.dependencies().has("mariadb")) {
			addManagedDriver(build.dependencies(), "org.mariadb", "r2dbc-mariadb", "1.1.3");
		}
		if (build.dependencies().has("mysql")) {
			addManagedDriver(build.dependencies(), "io.asyncer", "r2dbc-mysql");
		}
		if (build.dependencies().has("postgresql")) {
			addManagedDriver(build.dependencies(), "org.postgresql", "r2dbc-postgresql");
		}
		if (build.dependencies().has("sqlserver")) {
			addManagedDriver(build.dependencies(), "io.r2dbc", "r2dbc-mssql", "1.0.0.RELEASE");
		}
		if (build.dependencies().has("oracle")) {
			addManagedDriver(build.dependencies(), "com.oracle.database.r2dbc", "oracle-r2dbc");
		}
		if (build.dependencies().has("flyway") || build.dependencies().has("liquibase")) {
			addSpringJdbcIfNecessary(build);
		}
	}

	private void addManagedDriver(DependencyContainer dependencies, String groupId, String artifactId) {
		dependencies.add(artifactId, Dependency.withCoordinates(groupId, artifactId).scope(DependencyScope.RUNTIME));
	}

	private void addManagedDriver(DependencyContainer dependencies, String groupId, String artifactId, String version) {
		Builder<?> builder = Dependency.withCoordinates(groupId, artifactId).scope(DependencyScope.RUNTIME);
		if (version != null) {
			builder.version(VersionReference.ofValue(version));
		}
		dependencies.add(artifactId, builder);
	}

	private void addSpringJdbcIfNecessary(Build build) {
		boolean hasSpringJdbc = build.dependencies().ids().anyMatch(JDBC_DEPENDENCY_IDS::contains);
		if (!hasSpringJdbc) {
			build.dependencies().add("spring-jdbc", Dependency.withCoordinates("org.springframework", "spring-jdbc"));
		}
	}

}
