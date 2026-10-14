/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.dgs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * A {@link ProjectContributor} that creates the "graphql-client" resources directory when
 * the DGS Codegen build plugin is requested.
 *
 * @author Brian Clozel
 */
class DgsCodegenProjectContributor implements ProjectContributor {

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path graphQlDirectory = projectRoot.resolve("src/main/resources/graphql-client");
		Files.createDirectories(graphQlDirectory);
	}

}
