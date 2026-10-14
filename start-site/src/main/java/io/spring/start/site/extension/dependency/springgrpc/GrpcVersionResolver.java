/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import java.util.Map;

import io.spring.initializr.versionresolver.MavenVersionResolver;

import org.springframework.util.function.SingletonSupplier;

/**
 * Resolves dependency versions from 'org.springframework.grpc:spring-grpc-dependencies'.
 *
 * @author Moritz Halbritter
 */
class GrpcVersionResolver {

	private final SingletonSupplier<Map<String, String>> versions;

	GrpcVersionResolver(MavenVersionResolver versionResolver, String springGrpcVersion) {
		this.versions = SingletonSupplier.of(() -> versionResolver.resolveDependencies("org.springframework.grpc",
				"spring-grpc-dependencies", springGrpcVersion));
	}

	String resolveProtobufJavaVersion() {
		return this.versions.obtain().get("com.google.protobuf:protobuf-java");
	}

	String resolveGrpcVersion() {
		return this.versions.obtain().get("io.grpc:grpc-core");
	}

}
