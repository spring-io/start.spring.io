/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradleExtensionContainer;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Abstract base class for {@link BuildCustomizer} to deal withj Gradle based gRPC
 * projects.
 *
 * @author Moritz Halbritter
 */
abstract class AbstractGrpcGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final String pluginVersion;

	private final char quote;

	AbstractGrpcGradleBuildCustomizer(char quote, String pluginVersion) {
		this.pluginVersion = pluginVersion;
		this.quote = quote;
	}

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("com.google.protobuf", (plugin) -> plugin.setVersion(this.pluginVersion));
		customizeExtensions(build.extensions());
	}

	protected void customizeExtensions(GradleExtensionContainer extensions) {
	}

	protected String quote(String value) {
		return this.quote + value + this.quote;
	}

}
