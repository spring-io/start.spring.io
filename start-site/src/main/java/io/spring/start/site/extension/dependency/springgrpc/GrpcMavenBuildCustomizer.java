/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to add the 'protobuf-maven-plugin' plugin.
 *
 * @author Moritz Halbritter
 */
class GrpcMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		build.plugins().add("io.github.ascopes", "protobuf-maven-plugin");
	}

}
