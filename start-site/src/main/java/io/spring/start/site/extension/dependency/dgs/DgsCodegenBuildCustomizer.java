/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.core.Ordered;

/**
 * {@link BuildCustomizer} that removes the "dgs-codegen" dependency as this is a build
 * plugin only dependency.
 *
 * @author Brian Clozel
 */
class DgsCodegenBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		build.dependencies().remove("dgs-codegen");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 10;
	}

}
