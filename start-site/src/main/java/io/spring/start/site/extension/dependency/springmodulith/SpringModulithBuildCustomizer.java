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
 * @author Stephane Nicoll
 */
class SpringModulithBuildCustomizer implements BuildCustomizer<Build> {

	private static final Collection<String> OBSERVABILITY_DEPENDENCIES = List.of("actuator", "datadog", "graphite",
			"influx", "new-relic", "prometheus", "wavefront", "zipkin");

	private static final Collection<String> PERSISTENCE = List.of("jdbc", "jpa", "mongodb");

	@Override
	public void customize(Build build) {
		DependencyContainer dependencies = build.dependencies();
		if (dependencies.has("actuator")) {
			dependencies.add("modulith-actuator", modulithDependency("actuator").scope(DependencyScope.RUNTIME));
		}
		if (OBSERVABILITY_DEPENDENCIES.stream().anyMatch(dependencies::has)) {
			dependencies.add("modulith-observability",
					modulithDependency("observability").scope(DependencyScope.RUNTIME));
		}

		boolean persistenceBackendAdded = addEventPublicationRegistryBackend(build);

		if (persistenceBackendAdded) {
			dependencies.remove("modulith");
		}

		dependencies.add("modulith-starter-test",
				modulithDependency("starter-test").scope(DependencyScope.TEST_COMPILE));
	}

	private boolean addEventPublicationRegistryBackend(Build build) {
		DependencyContainer dependencies = build.dependencies();
		return PERSISTENCE.stream()
			.map((it) -> addPersistenceDependency(it, dependencies))
			.reduce(false, (l, r) -> l || r);
	}

	private boolean addPersistenceDependency(String store, DependencyContainer dependencies) {
		if (!dependencies.has("data-" + store)) {
			return false;
		}
		dependencies.add("modulith-starter-" + store, modulithDependency("starter-" + store));
		return true;
	}

	private Builder<?> modulithDependency(String name) {
		return Dependency.withCoordinates("org.springframework.modulith", "spring-modulith-" + name);
	}

}
