/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.flyway;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Determine the appropriate Flyway dependency according to the database.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class FlywayBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		DependencyContainer dependencies = build.dependencies();
		if ((dependencies.has("mysql") || dependencies.has("mariadb"))) {
			dependencies.add("flyway-mysql", "org.flywaydb", "flyway-mysql", DependencyScope.COMPILE);
		}
		if (dependencies.has("sqlserver")) {
			dependencies.add("flyway-sqlserver", "org.flywaydb", "flyway-sqlserver", DependencyScope.COMPILE);
		}
		if (dependencies.has("oracle")) {
			dependencies.add("flyway-oracle", "org.flywaydb", "flyway-database-oracle", DependencyScope.COMPILE);
		}
		if (dependencies.has("db2")) {
			dependencies.add("flyway-database-db2", "org.flywaydb", "flyway-database-db2", DependencyScope.COMPILE);
		}
		if (dependencies.has("derby")) {
			dependencies.add("flyway-database-derby", "org.flywaydb", "flyway-database-derby", DependencyScope.COMPILE);
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
