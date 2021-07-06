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

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.code.TestApplicationTypeCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that use observability.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
class ObservabilityProjectGenerationConfiguration {

	@Bean
	ObservabilityBuildCustomizer observabilityBuildCustomizer() {
		return new ObservabilityBuildCustomizer();
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnRequestedDependency("wavefront")
	static class Wavefront {

		@Bean
		WavefrontHelpDocumentCustomizer wavefrontHelpDocumentCustomizer(Build build) {
			return new WavefrontHelpDocumentCustomizer(build);
		}

		@Bean
		TestApplicationTypeCustomizer<?> wavefrontTestApplicationTypeCustomizer() {
			return (typeDeclaration) -> typeDeclaration.annotate(Annotation
					.name("org.springframework.test.context.TestPropertySource", (ann) -> ann.attribute("properties",
							String.class, "management.metrics.export.wavefront.enabled=false")));
		}

	}

}
