/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.start.site.extension.dependency.springdata;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring Data.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
public class SpringDataProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("data-r2dbc")
	public R2dbcBuildCustomizer r2dbcBuildCustomizer(ProjectDescription description) {
		return new R2dbcBuildCustomizer(description.getPlatformVersion());
	}

	@Bean
	@ConditionalOnRequestedDependency("data-r2dbc")
	public R2dbcHelpDocumentCustomizer r2dbcHelpDocumentCustomizer(Build build, ProjectDescription description) {
		return new R2dbcHelpDocumentCustomizer(build, description.getPlatformVersion());
	}

}
