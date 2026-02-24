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

package io.spring.start.site.extension.dependency.springdocopenapi;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

/**
 * Configuration for generation of projects that depend on SpringDoc OpenAPI.
 *
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("springdoc-openapi")
public class SpringDocOpenApiProjectGenerationConfiguration {

	@Bean
	BuildCustomizer<Build> springDocOpenApiBuildCustomizer() {
		return (build) -> {
			boolean hasWebflux = build.dependencies().has("webflux");
			boolean hasWebMvc = build.dependencies().has("web");
			if (!hasWebflux && !hasWebMvc) {
				build.dependencies().add("web");
			}
			else if (!hasWebMvc && hasWebflux) {
				Dependency dependency = build.dependencies().get("springdoc-openapi");
				Assert.state(dependency != null, "'dependency' must not be null");
				build.dependencies()
					.add("springdoc-openapi",
							Dependency.from(dependency).artifactId("springdoc-openapi-starter-webflux-ui").build());
			}
		};
	};

}
