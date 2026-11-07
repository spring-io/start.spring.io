/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.generator.buildsystem.gradle.GradleExtensionContainer;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to apply the 'com.google.protobuf' plugin for the Kotlin DSL
 * and to declare its {@code grpc} plugin, which Spring Boot only configures once it is
 * declared.
 *
 * @author Moritz Halbritter
 * @author Bach Luu
 */
class GrpcGradleKotlinBuildCustomizer extends AbstractGrpcGradleBuildCustomizer {

	GrpcGradleKotlinBuildCustomizer(String pluginVersion) {
		super('\"', pluginVersion);
	}

	@Override
	protected void customizeExtensions(GradleExtensionContainer extensions) {
		extensions.customize("protobuf", (protobuf) -> {
			protobuf.importType("com.google.protobuf.gradle.id");
			protobuf.nested("plugins", (plugins) -> plugins.invoke("id", quote("grpc")));
		});
	}

}
