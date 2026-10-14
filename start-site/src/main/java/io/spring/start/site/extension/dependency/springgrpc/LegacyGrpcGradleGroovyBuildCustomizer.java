/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.generator.buildsystem.gradle.GradleExtensionContainer;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to customize the Groovy DSL Gradle build to build gRPC
 * projects.
 *
 * @author Moritz Halbritter
 */
class LegacyGrpcGradleGroovyBuildCustomizer extends AbstractGrpcGradleBuildCustomizer {

	LegacyGrpcGradleGroovyBuildCustomizer(String pluginVersion) {
		super('\'', pluginVersion);
	}

	@Override
	protected void customizeExtensions(GradleExtensionContainer extensions) {
		extensions.customize("protobuf", (protobuf) -> {
			protobuf.nested("protoc", (protoc) -> protoc.attribute("artifact", quote("com.google.protobuf:protoc")));
			protobuf.nested("plugins", (plugins) -> plugins.nested("grpc",
					(grpc) -> grpc.attribute("artifact", quote("io.grpc:protoc-gen-grpc-java"))));
			protobuf.nested("generateProtoTasks", (generateProtoTasks) -> generateProtoTasks.nested("all()*.plugins",
					(plugins) -> plugins.nested("grpc", (grpc) -> grpc.invoke("option", quote("@generated=omit")))));
		});
	}

}
