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

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionProperty;
import io.spring.initializr.generator.version.VersionReference;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring AI Docker Compose.
 *
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("docker-compose")
@ConditionalOnRequestedSpringAiDependency
public class SpringAiDockerComposeProjectGenerationConfiguration {

	/**
	 * Dependency id of
	 * {@code org.springframework.ai:spring-ai-spring-boot-docker-compose}.
	 */
	public static final String DEPENDENCY_ID = "spring-ai-docker-compose";

	@Bean
	BuildCustomizer<Build> springAiDockerComposeBuildCustomizer(InitializrMetadata metadata,
			ProjectDescription description) {
		// spring-ai-spring-boot-docker-compose is not managed in the BOM
		// See https://github.com/spring-projects/spring-ai/issues/1314
		VersionProperty springAiBomVersion = getSpringAiVersion(metadata, description);
		return (build) -> build.dependencies()
			.add(DEPENDENCY_ID,
					Dependency.withCoordinates("org.springframework.ai", "spring-ai-spring-boot-docker-compose")
						.version(VersionReference.ofProperty(springAiBomVersion))
						.scope(DependencyScope.RUNTIME));
	}

	private static VersionProperty getSpringAiVersion(InitializrMetadata metadata, ProjectDescription description) {
		return metadata.getConfiguration().getEnv().getBoms().get("spring-ai").getVersionProperty();
	}

}
