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

package io.spring.start.site.extension.dependency.observability;

import java.util.Set;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Adds the actuator if necessary.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class ObservabilityActuatorBuildCustomizer implements BuildCustomizer<Build> {

	private static final VersionRange SPRING_BOOT_4_OR_LATER = VersionParser.DEFAULT.parseRange("4.0.0-RC1");

	private static final Set<String> TRACING = Set.of("distributed-tracing", "zipkin");

	private static final Set<String> PUSH_BASED_METRICS = Set.of("datadog", "dynatrace", "graphite", "influx",
			"new-relic", "otlp-metrics", "wavefront");

	private static final Set<String> PULL_BASED_METRICS = Set.of("prometheus");

	private final Version bootVersion;

	ObservabilityActuatorBuildCustomizer(Version bootVersion) {
		this.bootVersion = bootVersion;
	}

	@Override
	public void customize(Build build) {
		if (!hasActuator(build) && needsActuator(build)) {
			build.dependencies().add("actuator");
		}
		if (isBoot4OrLater() && hasPushBasedMetrics(build) && !hasActuator(build)) {
			build.dependencies()
				.add("spring-boot-micrometer-metrics", "org.springframework.boot", "spring-boot-micrometer-metrics",
						DependencyScope.COMPILE);
			build.dependencies()
				.add("spring-boot-micrometer-metrics-test", "org.springframework.boot",
						"spring-boot-micrometer-metrics-test", DependencyScope.TEST_COMPILE);
		}
	}

	private boolean hasActuator(Build build) {
		return build.dependencies().has("actuator");
	}

	private boolean needsActuator(Build build) {
		if (isBoot4OrLater()) {
			return hasTracing(build) || hasPullBasedMetrics(build);
		}
		else {
			return hasTracing(build) || hasPullBasedMetrics(build) || hasPushBasedMetrics(build);
		}
	}

	private boolean isBoot4OrLater() {
		return SPRING_BOOT_4_OR_LATER.match(this.bootVersion);
	}

	private boolean hasPullBasedMetrics(Build build) {
		return build.dependencies().ids().anyMatch(PULL_BASED_METRICS::contains);
	}

	private boolean hasPushBasedMetrics(Build build) {
		return build.dependencies().ids().anyMatch(PUSH_BASED_METRICS::contains);
	}

	private boolean hasTracing(Build build) {
		return build.dependencies().ids().anyMatch(TRACING::contains);
	}

}
