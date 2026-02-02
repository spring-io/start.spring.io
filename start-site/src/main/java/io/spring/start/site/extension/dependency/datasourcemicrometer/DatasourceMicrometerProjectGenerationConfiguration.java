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
