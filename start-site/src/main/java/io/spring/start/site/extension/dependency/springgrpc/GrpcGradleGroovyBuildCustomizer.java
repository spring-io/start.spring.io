/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.generator.buildsystem.gradle.GradleExtensionContainer;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to apply the 'com.google.protobuf' plugin for the Groovy DSL
 * and to declare its {@code grpc} plugin, which Spring Boot only configures once it is
 * declared.
 *
 * @author Moritz Halbritter
 * @author Bach Luu
 */
class GrpcGradleGroovyBuildCustomizer extends AbstractGrpcGradleBuildCustomizer {

	GrpcGradleGroovyBuildCustomizer(String pluginVersion) {
		super('\'', pluginVersion);
	}

	@Override
	protected void customizeExtensions(GradleExtensionContainer extensions) {
		extensions.customize("protobuf",
				(protobuf) -> protobuf.nested("plugins", (plugins) -> plugins.nested("grpc", (grpc) -> {
				})));
	}

}
