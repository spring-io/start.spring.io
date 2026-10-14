/*
 * Copyright 2012 - present the original author or authors.
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
