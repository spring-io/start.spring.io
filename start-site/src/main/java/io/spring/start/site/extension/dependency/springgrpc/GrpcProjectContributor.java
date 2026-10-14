/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * A {@link ProjectContributor} that creates the "src/main/proto" directory.
 *
 * @author Moritz Halbritter
 */
class GrpcProjectContributor implements ProjectContributor {

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path protoDirectory = projectRoot.resolve("src/main/proto");
		Files.createDirectories(protoDirectory);
	}

}
