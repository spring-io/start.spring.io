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

package io.spring.start.site.extension.dependency.springmodulith;

import java.util.Collection;
import java.util.List;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.Dependency.Builder;
import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Registers Spring Modulith dependencies to the build file of the project to be created,
 * in particular registering integration dependencies depending on others registered for
 * inclusion as well (observability and persistence).
 *
 * @author Oliver Drotbohm
 */
class SpringModulithBuildCustomizer implements BuildCustomizer<Build> {

	static final String GROUP_ID = "org.springframework.experimental";
	static final String BOM_ARTIFACT_ID = "spring-modulith-bom";

	static final Collection<String> OBSERVABILITY_DEPENDENCIES = List.of("datadog", "graphite", "influx", "new-relic",
			"prometheus", "wavefront", "zipkin");

	private static final String ARTIFACT_PREFIX = "spring-modulith-";

	private static final Builder<?> ACTUATOR = Dependency.withCoordinates(GROUP_ID, ARTIFACT_PREFIX + "actuator")
		.scope(DependencyScope.RUNTIME);

	private static final Builder<?> OBSERVABILITY = Dependency
		.withCoordinates(GROUP_ID, ARTIFACT_PREFIX + "observability")
		.scope(DependencyScope.RUNTIME);

	private static final Builder<?> STARTER_JDBC = Dependency.withCoordinates(GROUP_ID,
			ARTIFACT_PREFIX + "starter-jdbc");

	private static final Builder<?> STARTER_JPA = Dependency.withCoordinates(GROUP_ID, ARTIFACT_PREFIX + "starter-jpa");

	private static final Builder<?> STARTER_MONGODB = Dependency.withCoordinates(GROUP_ID,
			ARTIFACT_PREFIX + "starter-mongodb");

	private static final Builder<?> TEST = Dependency.withCoordinates(GROUP_ID, ARTIFACT_PREFIX + "starter-test")
		.scope(DependencyScope.TEST_COMPILE);

	@Override
	public void customize(Build build) {
		DependencyContainer dependencies = build.dependencies();

		// Actuator

		if (dependencies.has("actuator")) {
			dependencies.add("modulith-actuator", ACTUATOR);
		}

		// Observability

		if (OBSERVABILITY_DEPENDENCIES.stream().anyMatch(dependencies::has)) {
			dependencies.add("modulith-observability", OBSERVABILITY);
		}

		// Event publication registry support

		addEventPublicationRegistryBackend(build);

		dependencies.add("modulith-starter-test", TEST);
	}

	private void addEventPublicationRegistryBackend(Build build) {

		DependencyContainer dependencies = build.dependencies();

		if (dependencies.has("data-mongodb")) {
			dependencies.add("modulith-starter-mongodb", STARTER_MONGODB);
		}

		if (dependencies.has("data-jdbc")) {
			dependencies.add("modulith-starter-jdbc", STARTER_JDBC);
		}

		if (dependencies.has("data-jpa")) {
			dependencies.add("modulith-starter-jpa", STARTER_JPA);
		}
	}

}
