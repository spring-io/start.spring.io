/*
 * Copyright 2012-2020 the original author or authors.
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
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.dependency.devtools.DevToolsGradleBuildCustomizer;
import io.spring.initializr.generator.spring.dependency.devtools.DevToolsMavenBuildCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for customizations relevant for Spring Boot.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
public class SpringBootProjectGenerationConfiguration {

	private static final String DEVTOOLS_ID = "devtools";

	@Bean
	@ConditionalOnRequestedDependency(DEVTOOLS_ID)
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	public DevToolsMavenBuildCustomizer devToolsMavenBuildCustomizer() {
		return new DevToolsMavenBuildCustomizer(DEVTOOLS_ID);
	}

	@Bean
	@ConditionalOnRequestedDependency(DEVTOOLS_ID)
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	public DevToolsGradleBuildCustomizer devToolsGradleBuildCustomizer(ProjectDescription projectDescription) {
		return new DevToolsGradleBuildCustomizer(projectDescription.getPlatformVersion(), DEVTOOLS_ID);
	}

}
