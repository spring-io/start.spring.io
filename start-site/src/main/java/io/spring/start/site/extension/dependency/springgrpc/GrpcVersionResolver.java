/*
 * Copyright 2012 - present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
