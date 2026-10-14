/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graphql;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * A {@link ProjectContributor} that creates the "graphql" resources directory when Spring
 * for GraphQL is requested.
 *
 * @author Andy Wilkinson
 */
class SpringGraphQlProjectContributor implements ProjectContributor {

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path graphQlDirectory = projectRoot.resolve("src/main/resources/graphql");
		Files.createDirectories(graphQlDirectory);
	}

}
