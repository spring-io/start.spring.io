/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.datasourcemicrometer;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for projects that depend on Datasource
 * Micrometer.
 *
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("datasource-micrometer")
class DatasourceMicrometerProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("opentelemetry")
	BuildCustomizer<Build> datasourceMicrometerAndOpenTelemetryBuildCustomizer() {
		return (build) -> build.dependencies()
			.add("datasource-micrometer-opentelemetry", "net.ttddyy.observation", "datasource-micrometer-opentelemetry",
					DependencyScope.COMPILE);
	}

}
