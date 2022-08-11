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

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependencyBuildCustomizer;
import io.spring.start.site.support.implicit.ImplicitDependencyHelpDocumentCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for customizations relevant to selected
 * dependencies.
 *
 * @author Yonghui Ye
 * @author Andy Wilkinson
 */
@ProjectGenerationConfiguration
class SpringAzureProjectGenerationConfiguration {

	private final Iterable<ImplicitDependency> azureDependencies = SpringAzureModuleRegistry.create();

	@Bean
	ImplicitDependencyBuildCustomizer azureDependencyBuildCustomizer() {
		return new ImplicitDependencyBuildCustomizer(this.azureDependencies);
	}

	@Bean
	ImplicitDependencyHelpDocumentCustomizer azureDependencyHelpDocumentCustomizer(Build build) {
		return new ImplicitDependencyHelpDocumentCustomizer(this.azureDependencies, build);
	}

}
