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

	private static final String GRPC_PLUGIN_VERSION = "0.9.4";

	private final char quote;

	AbstractGrpcGradleBuildCustomizer(char quote) {
		this.quote = quote;
	}

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("com.google.protobuf", (plugin) -> plugin.setVersion(GRPC_PLUGIN_VERSION));
		customizeExtensions(build.extensions());
	}

	protected abstract void customizeExtensions(GradleExtensionContainer extensions);

	protected String quote(String value) {
		return this.quote + value + this.quote;
	}

}
