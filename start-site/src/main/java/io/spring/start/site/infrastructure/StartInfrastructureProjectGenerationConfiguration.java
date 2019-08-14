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

package io.spring.start.site.infrastructure;

import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.code.kotlin.InitializrMetadataKotlinVersionResolver;
import io.spring.initializr.generator.spring.code.kotlin.KotlinVersionResolver;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for infrastructure that tune how metadata is
 * resolved.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
public class StartInfrastructureProjectGenerationConfiguration {

	@Bean
	public KotlinVersionResolver kotlinVersionResolver(DependencyManagementVersionResolver versionResolver,
			InitializrMetadata metadata) {
		return new ManagedDependenciesKotlinVersionResolver(versionResolver,
				(description) -> new InitializrMetadataKotlinVersionResolver(metadata)
						.resolveKotlinVersion(description));
	}

}
