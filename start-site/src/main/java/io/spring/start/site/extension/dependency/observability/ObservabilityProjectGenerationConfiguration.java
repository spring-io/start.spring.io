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

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that use observability.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
class ObservabilityProjectGenerationConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnPlatformVersion("3.0.0-M1")
	static class ObservabilityConfiguration {

		@Bean
		ObservabilityActuatorBuildCustomizer observabilityActuatorBuildCustomizer() {
			return new ObservabilityActuatorBuildCustomizer();
		}

		@Bean
		ObservabilityDistributedTracingBuildCustomizer observabilityDistributedTracingBuildCustomizer() {
			return new ObservabilityDistributedTracingBuildCustomizer();
		}

		@Bean
		ObservabilityHelpDocumentCustomizer observabilityHelpDocumentCustomizer(ProjectDescription description,
				Build build) {
			return new ObservabilityHelpDocumentCustomizer(description, build);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnPlatformVersion("[2.0.0,3.0.0-M1)")
	static class Observability2xConfiguration {

		@Bean
		Observability2xHelpDocumentCustomizer observabilityHelpDocumentCustomizer(Build build) {
			return new Observability2xHelpDocumentCustomizer(build);
		}

	}

}
