/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to apply the 'com.google.protobuf' plugin for the Kotlin DSL.
 *
 * @author Moritz Halbritter
 */
class GrpcGradleKotlinBuildCustomizer extends AbstractGrpcGradleBuildCustomizer {

	GrpcGradleKotlinBuildCustomizer(String pluginVersion) {
		super('\"', pluginVersion);
	}

}
