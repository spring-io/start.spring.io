/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that depend on Spring
 * REST Docs.
 *
 * @author Andy Wilkinson
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("restdocs")
public class SpringRestDocsProjectGenerationConfiguration {

	@Bean
	public SpringRestDocsBuildCustomizer springRestDocsBuildCustomizer() {
		return new SpringRestDocsBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	public SpringRestDocsGradleBuildCustomizer restDocsGradleBuildCustomizer() {
		return new SpringRestDocsGradleBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	public SpringRestDocsMavenBuildCustomizer restDocsMavenBuildCustomizer() {
		return new SpringRestDocsMavenBuildCustomizer();
	}

}
