/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.liquibase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * A {@link ProjectContributor} that creates the "db/changelog" resources directory when
 * Liquibase is requested.
 *
 * @author Eddú Meléndez
 */
public class LiquibaseProjectContributor implements ProjectContributor {

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path changelogDirectory = projectRoot.resolve("src/main/resources/db/changelog");
		Files.createDirectories(changelogDirectory);
	}

}
