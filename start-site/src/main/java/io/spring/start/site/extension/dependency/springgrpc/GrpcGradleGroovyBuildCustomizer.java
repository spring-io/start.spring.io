/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to apply the 'com.google.protobuf' plugin for the Groovy DSL.
 *
 * @author Moritz Halbritter
 */
class GrpcGradleGroovyBuildCustomizer extends AbstractGrpcGradleBuildCustomizer {

	GrpcGradleGroovyBuildCustomizer(String pluginVersion) {
		super('\'', pluginVersion);
	}

}
