/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.core.Ordered;

/**
 * {@link BuildCustomizer} that removes the "native" dependency as GraalVM support does
 * not require any dependency.
 *
 * @author Stephane Nicoll
 */
class GraalVmBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		build.dependencies().remove("native");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 10;
	}

}
