/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.support.implicit;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that customize the build if necessary based on
 * {@link ImplicitDependency implicit dependencies}.
 *
 * @author Stephane Nicoll
 */
public class ImplicitDependencyBuildCustomizer implements BuildCustomizer<Build> {

	private final Iterable<ImplicitDependency> dependencies;

	public ImplicitDependencyBuildCustomizer(Iterable<ImplicitDependency> dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public void customize(Build build) {
		for (ImplicitDependency dependency : this.dependencies) {
			dependency.customize(build);
		}
	}

}
