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

	@Bean
	GrpcAdditionalDependenciesBuildCustomizer grpcAdditionalDependenciesBuildCustomizer() {
		return new GrpcAdditionalDependenciesBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
	GrpcGradleGroovyBuildCustomizer grpcGradleGroovyBuildCustomizer() {
		return new GrpcGradleGroovyBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
	GrpcGradleKotlinBuildCustomizer grpcGradleKotlinBuildCustomizer() {
		return new GrpcGradleKotlinBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	GrpcMavenBuildCustomizer grpcMavenBuildCustomizer(GrpcVersionResolver versionResolver) {
		return new GrpcMavenBuildCustomizer(versionResolver.resolveProtobufJavaVersion(),
				versionResolver.resolveGrpcVersion());
	}

	@Bean
	GrpcProjectContributor grpcProjectContributor() {
		return new GrpcProjectContributor();
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
