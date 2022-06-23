/*
 * Copyright 2012-2021 the original author or authors.
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
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for customizations relevant to selected
 * dependencies.
 *
 * @author Yonghui Ye
 */
@ProjectGenerationConfiguration
public class SpringAzureProjectGenerationConfiguration {

	private final InitializrMetadata metadata;

	private final ProjectDescription description;

	public SpringAzureProjectGenerationConfiguration(InitializrMetadata metadata, ProjectDescription description) {
		this.metadata = metadata;
		this.description = description;
	}

	@Bean
	public SpringAzureDefaultBuildCustomizer springAzureBuildCustomizer() {
		return new SpringAzureDefaultBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("actuator")
	public SpringAzureActuatorBuildCustomizer springAzureActuatorBuildCustomizer() {
		return new SpringAzureActuatorBuildCustomizer();
	}

	@Bean
	@ConditionalOnRequestedDependency("kafka")
	public SpringAzureKafkaBuildCustomizer springAzureKafkaBuildCustomizer() {
		return new SpringAzureKafkaBuildCustomizer();
	}

	@Bean
	public SpringAzureIntegrationStorageQueueBuildCustomizer springAzureMessagingBuildCustomizer() {
		return new SpringAzureIntegrationStorageQueueBuildCustomizer(this.metadata, this.description);
	}

	@Bean
	@ConditionalOnRequestedDependency("cloud-starter-sleuth")
	public SpringAzureSleuthBuildCustomizer springAzureSleuthBuildCustomizer() {
		return new SpringAzureSleuthBuildCustomizer();
	}

	@Bean
	public SpringAzureHelpDocumentCustomizer springAzureHelpDocumentCustomizer(Build build) {
		return new SpringAzureHelpDocumentCustomizer(build);
	}

}
