/*
 * Copyright 2012-2024 the original author or authors.
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

import io.spring.initializr.generator.buildsystem.PropertyContainer;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenPluginContainer;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionProperty;

/**
 * {@link BuildCustomizer} to customize the Maven build to build gRPC projects.
 *
 * @author Moritz Halbritter
 */
class GrpcMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private static final String OS_PLUGIN_VERSION = "1.7.1";

	private static final String PROTOBUF_PLUGIN_VERSION = "0.6.1";

	private final String protobufJavaVersion;

	private final String grpcVersion;

	GrpcMavenBuildCustomizer(String protobufJavaVersion, String grpcVersion) {
		this.protobufJavaVersion = protobufJavaVersion;
		this.grpcVersion = grpcVersion;
	}

	@Override
	public void customize(MavenBuild build) {
		VersionProperty protobufJava = VersionProperty.of("protobuf-java.version");
		VersionProperty grpc = VersionProperty.of("grpc.version");
		addVersionProperties(build.properties(), protobufJava, grpc);
		addOsPlugin(build.plugins());
		addProtobufPlugin(build.plugins(), protobufJava, grpc);
	}

	private void addVersionProperties(PropertyContainer properties, VersionProperty protobufJava,
			VersionProperty grpc) {
		properties.version(protobufJava, this.protobufJavaVersion);
		properties.version(grpc, this.grpcVersion);
	}

	private void addOsPlugin(MavenPluginContainer plugins) {
		plugins.add("kr.motd.maven", "os-maven-plugin", (plugin) -> {
			plugin.version(OS_PLUGIN_VERSION);
			plugin.execution("initialize", (execution) -> {
				execution.phase("initialize");
				execution.goal("detect");
			});
		});
	}

	private void addProtobufPlugin(MavenPluginContainer plugins, VersionProperty protobufJava, VersionProperty grpc) {
		plugins.add("org.xolstice.maven.plugins", "protobuf-maven-plugin", (plugin) -> {
			plugin.version(PROTOBUF_PLUGIN_VERSION);
			plugin.configuration((configuration) -> {
				configuration.add("protocArtifact", "com.google.protobuf:protoc:${%s}:exe:${os.detected.classifier}"
					.formatted(protobufJava.toStandardFormat()));
				configuration.add("pluginId", "grpc-java");
				configuration.add("pluginArtifact", "io.grpc:protoc-gen-grpc-java:${%s}:exe:${os.detected.classifier}"
					.formatted(grpc.toStandardFormat()));
			});
			plugin.execution("compile", (execution) -> {
				execution.goal("compile").goal("compile-custom");
				execution.configuration(
						(configuration) -> configuration.add("pluginParameter", "jakarta_omit,@generated=omit"));
			});
		});
	}

}
