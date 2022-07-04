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
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for customizations relevant to selected
 * dependencies.
 *
 * @author Yonghui Ye
 */
@ProjectGenerationConfiguration
class SpringAzureProjectGenerationConfiguration {

	@Bean
	SpringAzureDefaultBuildCustomizer springAzureBuildCustomizer() {
		return new SpringAzureDefaultBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("actuator")
	SpringAzureActuatorBuildCustomizer springAzureActuatorBuildCustomizer() {
		return new SpringAzureActuatorBuildCustomizer();
	}

	@Bean
	SpringAzureIntegrationStorageQueueBuildCustomizer springAzureMessagingBuildCustomizer() {
		return new SpringAzureIntegrationStorageQueueBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("cloud-starter-sleuth")
	SpringAzureSleuthBuildCustomizer springAzureSleuthBuildCustomizer() {
		return new SpringAzureSleuthBuildCustomizer();
	}

	@Bean
	SpringAzureHelpDocumentCustomizer springAzureHelpDocumentCustomizer(Build build) {
		return new SpringAzureHelpDocumentCustomizer(build);
	}

}
