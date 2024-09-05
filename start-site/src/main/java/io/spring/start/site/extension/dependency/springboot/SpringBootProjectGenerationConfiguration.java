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
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.gradle.DevelopmentOnlyDependencyGradleBuildCustomizer;
import io.spring.initializr.generator.spring.build.maven.OptionalDependencyMavenBuildCustomizer;
import io.spring.start.site.extension.dependency.springai.SpringAiDockerComposeProjectGenerationConfiguration;

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

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	static class MavenConfiguration {

		@Bean
		@ConditionalOnRequestedDependency("devtools")
		OptionalDependencyMavenBuildCustomizer devToolsMavenBuildCustomizer() {
			return new OptionalDependencyMavenBuildCustomizer("devtools");
		}

		@Bean
		@ConditionalOnRequestedDependency("docker-compose")
		OptionalDependencyMavenBuildCustomizer dockerComposeMavenBuildCustomizer() {
			return new OptionalDependencyMavenBuildCustomizer("docker-compose");
		}

		/**
		 * Marks {@code org.springframework.ai:spring-ai-spring-boot-docker-compose} as
		 * optional.
		 * @return the build customizer
		 * @see SpringAiDockerComposeProjectGenerationConfiguration
		 */
		@Bean
		OptionalDependencyMavenBuildCustomizer springAiDockerComposeMavenBuildCustomizer() {
			return new OptionalDependencyMavenBuildCustomizer(
					SpringAiDockerComposeProjectGenerationConfiguration.DEPENDENCY_ID);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	static class GradleConfiguration {

		@Bean
		@ConditionalOnRequestedDependency("devtools")
		DevelopmentOnlyDependencyGradleBuildCustomizer devToolsGradleBuildCustomizer() {
			return new DevelopmentOnlyDependencyGradleBuildCustomizer("devtools");
		}

		@Bean
		@ConditionalOnRequestedDependency("docker-compose")
		DevelopmentOnlyDependencyGradleBuildCustomizer dockerComposeGradleBuildCustomizer() {
			return new DevelopmentOnlyDependencyGradleBuildCustomizer("docker-compose");
		}

		/**
		 * Puts {@code org.springframework.ai:spring-ai-spring-boot-docker-compose} in
		 * {@code developmentOnly} scope.
		 * @return the build customizer
		 * @see SpringAiDockerComposeProjectGenerationConfiguration
		 */
		@Bean
		DevelopmentOnlyDependencyGradleBuildCustomizer springAiDockerComposeGradleBuildCustomizer() {
			return new DevelopmentOnlyDependencyGradleBuildCustomizer(
					SpringAiDockerComposeProjectGenerationConfiguration.DEPENDENCY_ID);
		}

	}

}
