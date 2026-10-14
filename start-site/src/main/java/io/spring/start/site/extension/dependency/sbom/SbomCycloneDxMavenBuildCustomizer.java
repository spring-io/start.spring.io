/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.sbom;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} that adds the CycloneDX Maven plugin.
 *
 * @author Moritz Halbritter
 */
class SbomCycloneDxMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		build.plugins().add("org.cyclonedx", "cyclonedx-maven-plugin");
	}

}
