/*
 * Copyright 2012 - present the original author or authors.
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
	}

}
