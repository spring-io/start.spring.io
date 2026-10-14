/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.vaadin;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenDependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that adds a production profile to enable Vaadin's production
 * mode.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class VaadinMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		build.dependencies()
			.add("vaadin-dev", MavenDependency.withCoordinates("com.vaadin", "vaadin-dev").optional(true));
		build.plugins()
			.add("com.vaadin", "vaadin-maven-plugin", (plugin) -> plugin.version("${vaadin.version}")
				.execution("build-frontend", (execution) -> execution.goal("build-frontend")));
	}

}
