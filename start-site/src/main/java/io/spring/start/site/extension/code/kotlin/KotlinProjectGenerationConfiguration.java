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

package io.spring.start.site.extension.code.kotlin;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.code.kotlin.InitializrMetadataKotlinVersionResolver;
import io.spring.initializr.generator.spring.code.kotlin.KotlinVersionResolver;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that use the Kotlin
 * language.
 *
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnLanguage(KotlinLanguage.ID)
class KotlinProjectGenerationConfiguration {

	@Bean
	KotlinVersionResolver kotlinVersionResolver(DependencyManagementVersionResolver versionResolver,
			InitializrMetadata metadata) {
		return new ManagedDependenciesKotlinVersionResolver(versionResolver,
				(description) -> new InitializrMetadataKotlinVersionResolver(metadata)
						.resolveKotlinVersion(description));
	}

	@Bean
	ReactorKotlinExtensionsCustomizer reactorKotlinExtensionsCustomizer(InitializrMetadata metadata) {
		return new ReactorKotlinExtensionsCustomizer(metadata);
	}

	@Configuration
	static class KotlinCoroutinesCustomizerConfiguration {

		private final KotlinCoroutinesCustomizer customizer;

		KotlinCoroutinesCustomizerConfiguration(InitializrMetadata metadata, ProjectDescription description,
				DependencyManagementVersionResolver versionResolver) {
			this.customizer = new KotlinCoroutinesCustomizer(metadata, description, versionResolver);
		}

		@Bean
		BuildCustomizer<Build> kotlinCoroutinesBuildCustomizer() {
			return this.customizer::customize;
		}

		@Bean
		HelpDocumentCustomizer kotlinCoroutinesHelpDocumentCustomizer(Build build) {
			return (helpDocument) -> this.customizer.customize(helpDocument, build);
		}

	}

}
