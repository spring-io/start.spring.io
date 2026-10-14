/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.sbom;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.core.Ordered;

/**
 * {@link BuildCustomizer} that removes the CycloneDX SBOM dependency as SBOM support does
 * not require any dependency.
 *
 * @author Moritz Halbritter
 */
class SbomCycloneDxBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		build.dependencies().remove("sbom-cyclone-dx");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 10;
	}

}
