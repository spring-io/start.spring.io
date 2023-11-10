/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.springboot;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.gradle.DevelopmentOnlyDependencyGradleBuildCustomizer;
import io.spring.initializr.generator.spring.build.maven.OptionalDependencyMavenBuildCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ProjectGenerationConfiguration} for customizations relevant for Spring Boot.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
class SpringBootProjectGenerationConfiguration {

	private static final String DEVTOOLS_ID = "devtools";

	private static final String DOCKER_COMPOSE_ID = "docker-compose";

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	static class MavenConfiguration {

		@Bean
		@ConditionalOnPlatformVersion("[2.0.0,3.2.0-M1)")
		PaketoBuilderMavenBuildCustomizer paketoBuilderMavenBuildCustomizer() {
			return new PaketoBuilderMavenBuildCustomizer();
		}

		@Bean
		@ConditionalOnRequestedDependency(DEVTOOLS_ID)
		OptionalDependencyMavenBuildCustomizer devToolsMavenBuildCustomizer() {
			return new OptionalDependencyMavenBuildCustomizer(DEVTOOLS_ID);
		}

		@Bean
		@ConditionalOnRequestedDependency(DOCKER_COMPOSE_ID)
		OptionalDependencyMavenBuildCustomizer dockerComposeMavenBuildCustomizer() {
			return new OptionalDependencyMavenBuildCustomizer(DOCKER_COMPOSE_ID);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	static class GradleConfiguration {

		@Bean
		@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
		@ConditionalOnPlatformVersion("[2.0.0,3.2.0-M1)")
		PaketoBuilderGroovyDslGradleBuildCustomizer paketoBuilderGroovyDslGradleBuildCustomizer() {
			return new PaketoBuilderGroovyDslGradleBuildCustomizer();
		}

		@Bean
		@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
		@ConditionalOnPlatformVersion("[2.0.0,3.2.0-M1)")
		PaketoBuilderKotlinDslGradleBuildCustomizer paketoBuilderKotlinDslGradleBuildCustomizer() {
			return new PaketoBuilderKotlinDslGradleBuildCustomizer();
		}

		@Bean
		@ConditionalOnRequestedDependency(DEVTOOLS_ID)
		DevelopmentOnlyDependencyGradleBuildCustomizer devToolsGradleBuildCustomizer() {
			return new DevelopmentOnlyDependencyGradleBuildCustomizer(DEVTOOLS_ID);
		}

		@Bean
		@ConditionalOnRequestedDependency(DOCKER_COMPOSE_ID)
		DevelopmentOnlyDependencyGradleBuildCustomizer dockerComposeGradleBuildCustomizer() {
			return new DevelopmentOnlyDependencyGradleBuildCustomizer(DOCKER_COMPOSE_ID);
		}

	}

}
