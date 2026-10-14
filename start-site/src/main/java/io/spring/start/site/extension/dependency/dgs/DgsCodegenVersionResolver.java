/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * Resolve DGS Codegen plugin version to use based on the platform version and build
 * system.
 *
 * @author Brian Clozel
 */
abstract class DgsCodegenVersionResolver {

	static String resolve(InitializrMetadata metadata, Version platformVersion, BuildSystem build) {
		if (GradleBuildSystem.ID.equals(build.id())) {
			return metadata.getDependencies().get("dgs-codegen").resolve(platformVersion).getVersion();
		}
		else if (MavenBuildSystem.ID.equals(build.id())) {
			// https://github.com/deweyjose/graphqlcodegen/releases
			return "1.61.5";
		}
		throw new IllegalArgumentException("Could not resolve DGS Codegen version for build system " + build.id());
	}

}
