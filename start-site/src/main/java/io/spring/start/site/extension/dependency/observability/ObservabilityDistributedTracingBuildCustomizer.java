/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Configures distributed tracing if necessary.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class ObservabilityDistributedTracingBuildCustomizer implements BuildCustomizer<Build> {

	static final int ORDER = 0;

	private static final String DISTRIBUTED_TRACING = "distributed-tracing";

	@Override
	public void customize(Build build) {
		if (hasTracingProvider(build)) {
			build.dependencies().remove(DISTRIBUTED_TRACING);
		}
		else if (hasDistributedTracing(build)) {
			build.dependencies()
				.add("spring-boot-micrometer-tracing-brave",
						Dependency.withCoordinates("org.springframework.boot", "spring-boot-micrometer-tracing-brave"));
			build.dependencies()
				.add("spring-boot-micrometer-tracing-test",
						Dependency.withCoordinates("org.springframework.boot", "spring-boot-micrometer-tracing-test")
							.scope(DependencyScope.TEST_COMPILE));
		}
	}

	@Override
	public int getOrder() {
		return ORDER;
	}

	private boolean hasDistributedTracing(Build build) {
		return build.dependencies().has(DISTRIBUTED_TRACING);
	}

	private boolean hasTracingProvider(Build build) {
		return build.dependencies().has("zipkin") || build.dependencies().has("opentelemetry");
	}

}
