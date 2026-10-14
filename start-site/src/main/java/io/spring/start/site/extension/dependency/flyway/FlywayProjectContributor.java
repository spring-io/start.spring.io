/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.flyway;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * A {@link ProjectContributor} that creates the "db/migration" resources directory when
 * Flyway is requested.
 *
 * @author Stephane Nicoll
 */
class FlywayProjectContributor implements ProjectContributor {

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path migrationDirectory = projectRoot.resolve("src/main/resources/db/migration");
		Files.createDirectories(migrationDirectory);
	}

}
