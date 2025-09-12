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

import io.spring.initializr.generator.buildsystem.gradle.GradleExtensionContainer;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to customize the Kotlin DSL Gradle build to build gRPC
 * projects.
 *
 * @author Moritz Halbritter
 */
class GrpcGradleKotlinBuildCustomizer extends AbstractGrpcGradleBuildCustomizer {

	GrpcGradleKotlinBuildCustomizer() {
		super('\"');
	}

	@Override
	protected void customizeExtensions(GradleExtensionContainer extensions) {
		extensions.customize("protobuf", (protobuf) -> {
			protobuf.nested("protoc", (protoc) -> protoc.attribute("artifact", quote("com.google.protobuf:protoc")));
			protobuf.importType("com.google.protobuf.gradle.id");
			protobuf.nested("plugins", (plugins) -> plugins.nested("id(\"grpc\")",
					(grpc) -> grpc.attribute("artifact", quote("io.grpc:protoc-gen-grpc-java"))));
			protobuf.nested("generateProtoTasks",
					(generateProtoTasks) -> generateProtoTasks.nested("all().forEach",
							(forEach) -> forEach.nested("it.plugins", (plugins) -> plugins.nested("id(\"grpc\")",
									(grpc) -> grpc.invoke("option", quote("@generated=omit"))))));
		});
	}

}
