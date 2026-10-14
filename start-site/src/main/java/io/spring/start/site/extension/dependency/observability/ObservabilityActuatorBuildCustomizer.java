/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.observability;

import java.util.Set;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Adds the actuator if necessary.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class ObservabilityActuatorBuildCustomizer implements BuildCustomizer<Build> {

	private static final Set<String> PUSH_BASED_METRICS = Set.of("datadog", "dynatrace", "graphite", "influx",
			"new-relic", "otlp-metrics");

	private static final Set<String> PULL_BASED_METRICS = Set.of("prometheus");

	@Override
	public void customize(Build build) {
		if (!hasActuator(build) && needsActuator(build)) {
			build.dependencies().add("actuator");
		}
		if (hasPushBasedMetrics(build) && !hasActuator(build)) {
			build.dependencies()
				.add("spring-boot-starter-micrometer-metrics", "org.springframework.boot",
						"spring-boot-starter-micrometer-metrics", DependencyScope.COMPILE);
			build.dependencies()
				.add("spring-boot-starter-micrometer-metrics-test", "org.springframework.boot",
						"spring-boot-starter-micrometer-metrics-test", DependencyScope.TEST_COMPILE);
		}
	}

	@Override
	public int getOrder() {
		return ObservabilityDistributedTracingBuildCustomizer.ORDER + 1;
	}

	private boolean hasActuator(Build build) {
		return build.dependencies().has("actuator");
	}

	private boolean needsActuator(Build build) {
		return hasPullBasedMetrics(build);
	}

	private boolean hasPullBasedMetrics(Build build) {
		return build.dependencies().ids().anyMatch(PULL_BASED_METRICS::contains);
	}

	private boolean hasPushBasedMetrics(Build build) {
		return build.dependencies().ids().anyMatch(PUSH_BASED_METRICS::contains);
	}

}
