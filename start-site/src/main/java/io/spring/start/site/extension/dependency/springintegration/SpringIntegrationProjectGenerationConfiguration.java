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

package io.spring.start.site.extension.dependency.springintegration;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependencyBuildCustomizer;
import io.spring.start.site.support.implicit.ImplicitDependencyHelpDocumentCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Spring Integration.
 *
 * @author Stephane Nicoll
 * @author Artem Bilan
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("integration")
class SpringIntegrationProjectGenerationConfiguration {

	private final Iterable<ImplicitDependency> dependencies;

	SpringIntegrationProjectGenerationConfiguration(ProjectDescription projectDescription) {
		this.dependencies = SpringIntegrationModuleRegistry.create(projectDescription.getPlatformVersion());
	}

	@Bean
	ImplicitDependencyBuildCustomizer springIntegrationBuildCustomizer() {
		return new ImplicitDependencyBuildCustomizer(this.dependencies);
	}

	@Bean
	ImplicitDependencyHelpDocumentCustomizer springIntegrationHelpCustomizer(Build build) {
		return new ImplicitDependencyHelpDocumentCustomizer(this.dependencies, build);
	}

}
