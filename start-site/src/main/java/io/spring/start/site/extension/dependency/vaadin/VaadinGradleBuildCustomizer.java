/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.vaadin;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradleDependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * A {@link BuildCustomizer} that registers Vaadin's Gradle plugin.
 *
 * @author Stephane Nicoll
 */
class VaadinGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final String vaadinVersion;

	VaadinGradleBuildCustomizer(InitializrMetadata metadata, Version platformVersion) {
		this.vaadinVersion = metadata.getConfiguration()
			.getEnv()
			.getBoms()
			.get("vaadin")
			.resolve(platformVersion)
			.getVersion();
	}

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("com.vaadin", (plugin) -> plugin.setVersion(this.vaadinVersion));
		build.dependencies()
			.add("vaadin-dev",
					GradleDependency.withCoordinates("com.vaadin", "vaadin-dev").configuration("developmentOnly"));
	}

}
