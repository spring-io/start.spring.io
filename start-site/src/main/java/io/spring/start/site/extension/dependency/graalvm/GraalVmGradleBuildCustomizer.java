/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} abstraction for Gradle projects using GraalVM.
 *
 * @author Stephane Nicoll
 */
class GraalVmGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final String nbtVersion;

	protected GraalVmGradleBuildCustomizer(String nbtVersion) {
		this.nbtVersion = nbtVersion;
	}

	@Override
	public final void customize(GradleBuild build) {
		if (this.nbtVersion != null) {
			build.plugins().add("org.graalvm.buildtools.native", (plugin) -> plugin.setVersion(this.nbtVersion));
		}
	}

}
