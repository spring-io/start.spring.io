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

package io.spring.start.site.extension.dependency.springmodulith;

import java.util.Collection;
import java.util.List;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.Dependency.Builder;
import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Registers Spring Modulith dependencies to the build file of the project to be created,
 * in particular registering integration dependencies depending on others registered for
 * inclusion as well (observability and persistence).
 *
 * @author Oliver Drotbohm
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
class SpringModulithBuildCustomizer implements BuildCustomizer<Build> {

	private static final VersionRange SPRING_BOOT_4_1_OR_LATER = VersionParser.DEFAULT.parseRange("4.1.0-M1");

	private static final Collection<String> OBSERVABILITY_DEPENDENCIES = List.of("datadog", "graphite", "influx",
			"new-relic", "otlp-metrics", "prometheus", "zipkin");

	private static final Collection<String> PERSISTENCE = List.of("jdbc", "jpa", "mongodb", "neo4j");

	private static final Collection<String> BROKERS = List.of("activemq", "amqp", "artemis", "kafka");

	private final Version version;

	SpringModulithBuildCustomizer(Version version) {
		this.version = version;
	}

	@Override
	public void customize(Build build) {
		DependencyContainer dependencies = build.dependencies();
		addActuatorAndObservabilityDependencies(dependencies);
		if (dependencies.has("flyway")) {
			dependencies.add("modulith-runtime", modulithDependency("runtime").scope(DependencyScope.RUNTIME));
		}
		addEventPublicationRegistryBackend(build);
		if (addEventExternalizationDependency(build)) {
			dependencies.add("modulith-events-api", modulithDependency("events-api"));
		}
		dependencies.add("modulith-starter-test",
				modulithDependency("starter-test").scope(DependencyScope.TEST_COMPILE));
	}

	private void addActuatorAndObservabilityDependencies(DependencyContainer dependencies) {
		if (hasActuator(dependencies) && hasObservability(dependencies)) {
			dependencies.add("modulith-starter-insight",
					modulithDependency("starter-insight").scope(DependencyScope.COMPILE));
			return;
		}
		if (hasActuator(dependencies)) {
			dependencies.add("modulith-actuator", modulithDependency("actuator").scope(DependencyScope.RUNTIME));
		}
		if (hasActuator(dependencies) || hasObservability(dependencies)) {
			if (isBoot41orLater()) {
				dependencies.add("modulith-observability-api",
						modulithDependency("observability-api").scope(DependencyScope.COMPILE));
				dependencies.add("modulith-observability-core",
						modulithDependency("observability-core").scope(DependencyScope.RUNTIME));
			}
			else {
				dependencies.add("modulith-observability",
						modulithDependency("observability").scope(DependencyScope.RUNTIME));
			}
		}
	}

	private boolean isBoot41orLater() {
		return SPRING_BOOT_4_1_OR_LATER.match(this.version);
	}

	private boolean hasActuator(DependencyContainer dependencies) {
		return dependencies.has("actuator");
	}

	private boolean hasObservability(DependencyContainer dependencies) {
		return OBSERVABILITY_DEPENDENCIES.stream().anyMatch(dependencies::has);
	}

	private boolean addEventPublicationRegistryBackend(Build build) {
		DependencyContainer dependencies = build.dependencies();
		return PERSISTENCE.stream()
			.map((persistence) -> addPersistenceDependency(persistence, dependencies))
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

	private boolean addEventExternalizationDependency(Build build) {
		DependencyContainer dependencies = build.dependencies();
		return BROKERS.stream()
			.filter(dependencies::has)
			.map(this::getModulithBrokerKey)
			.peek((it) -> dependencies.add("modulith-events-" + it,
					modulithDependency("events-" + it).scope(DependencyScope.RUNTIME)))
			.findAny()
			.isPresent();
	}

	private String getModulithBrokerKey(String broker) {
		return switch (broker) {
			case "kafka", "amqp" -> broker;
			case "artemis", "activemq" -> "jms";
			default -> throw new IllegalArgumentException("Unsupported broker!");
		};
	}

}
