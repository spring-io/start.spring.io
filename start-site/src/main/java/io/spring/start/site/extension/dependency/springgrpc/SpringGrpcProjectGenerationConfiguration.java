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

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.start.site.extension.dependency.springgrpc.SpringGrpcProjectGenerationConfiguration.SpringGrpcCondition;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Spring gRPC.
 *
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@Conditional(SpringGrpcCondition.class)
class SpringGrpcProjectGenerationConfiguration {

	@Bean
	GrpcProjectContributor grpcProjectContributor() {
		return new GrpcProjectContributor();
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnPlatformVersion("4.1.0-M1")
	static class BootBuiltInSupport {

		private static final String GRPC_PLUGIN_VERSION = "0.9.6";

		@Bean
		@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
		GrpcGradleGroovyBuildCustomizer grpcGradleGroovyBuildCustomizer() {
			return new GrpcGradleGroovyBuildCustomizer(GRPC_PLUGIN_VERSION);
		}

		@Bean
		@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
		GrpcGradleKotlinBuildCustomizer grpcGradleKotlinBuildCustomizer() {
			return new GrpcGradleKotlinBuildCustomizer(GRPC_PLUGIN_VERSION);
		}

		@Bean
		@ConditionalOnBuildSystem(MavenBuildSystem.ID)
		GrpcMavenBuildCustomizer grpcMavenBuildCustomizer() {
			return new GrpcMavenBuildCustomizer();
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnPlatformVersion("[3.5.0,4.1.0-M1)")
	static class LegacyGrpcSupport {

		private static final String GRPC_PLUGIN_VERSION = "0.9.5";

		@Bean
		GrpcAdditionalDependenciesBuildCustomizer grpcAdditionalDependenciesBuildCustomizer() {
			return new GrpcAdditionalDependenciesBuildCustomizer();
		}

		@Bean
		@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
		LegacyGrpcGradleGroovyBuildCustomizer legacyGrpcGradleGroovyBuildCustomizer() {
			return new LegacyGrpcGradleGroovyBuildCustomizer(GRPC_PLUGIN_VERSION);
		}

		@Bean
		@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
		LegacyGrpcGradleKotlinBuildCustomizer legacyGrpcGradleKotlinBuildCustomizer() {
			return new LegacyGrpcGradleKotlinBuildCustomizer(GRPC_PLUGIN_VERSION);
		}

		@Bean
		@ConditionalOnBuildSystem(MavenBuildSystem.ID)
		LegacyGrpcMavenBuildCustomizer legacyGrpcMavenBuildCustomizer(GrpcVersionResolver grpcVersionResolver) {
			return new LegacyGrpcMavenBuildCustomizer(grpcVersionResolver.resolveProtobufJavaVersion(),
					grpcVersionResolver.resolveGrpcVersion());
		}

		@Bean
		GrpcVersionResolver grpcVersionResolver(ProjectDescription description, InitializrMetadata metadata,
				MavenVersionResolver versionResolver) {
			String springGrpcVersion = metadata.getConfiguration()
				.getEnv()
				.getBoms()
				.get("spring-grpc")
				.resolve(description.getPlatformVersion())
				.getVersion();
			return new GrpcVersionResolver(versionResolver, springGrpcVersion);
		}

		@Configuration(proxyBeanMethods = false)
		@ConditionalOnRequestedDependency("spring-grpc-server")
		static class GrpcServerConfiguration {

			@Bean
			@ConditionalOnRequestedDependency("web")
			GrpcServerWebMvcBuildCustomizer grpcMvcBuildCustomizer() {
				return new GrpcServerWebMvcBuildCustomizer();
			}

		}

	}

	static class SpringGrpcCondition extends AnyNestedCondition {

		SpringGrpcCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnRequestedDependency("spring-grpc-server")
		static class SpringGrpcServer {

		}

		@ConditionalOnRequestedDependency("spring-grpc-client")
		static class SpringGrpcClient {

		}

	}

}
