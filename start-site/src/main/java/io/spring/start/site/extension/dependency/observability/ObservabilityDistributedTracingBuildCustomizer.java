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

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Configures distributed tracing if necessary.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class ObservabilityDistributedTracingBuildCustomizer implements BuildCustomizer<Build> {

	static final int ORDER = 0;

	private static final VersionRange SPRING_BOOT_4_OR_LATER = VersionParser.DEFAULT.parseRange("4.0.0-M1");

	private static final String DISTRIBUTED_TRACING = "distributed-tracing";

	private final Version bootVersion;

	ObservabilityDistributedTracingBuildCustomizer(Version bootVersion) {
		this.bootVersion = bootVersion;
	}

	@Override
	public void customize(Build build) {
		if (isBoot4OrLater()) {
			handleBoot4(build);
		}
		else {
			handleBoot35(build);
		}
	}

	@Override
	public int getOrder() {
		return ORDER;
	}

	private void handleBoot4(Build build) {
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

	private void handleBoot35(Build build) {
		if (hasZipkin(build) && !hasDistributedTracing(build)) {
			build.dependencies().add(DISTRIBUTED_TRACING);
		}
		if (build.dependencies().has("wavefront") && hasDistributedTracing(build)) {
			build.dependencies()
				.add("wavefront-tracing-reporter",
						Dependency.withCoordinates("io.micrometer", "micrometer-tracing-reporter-wavefront")
							.scope(DependencyScope.RUNTIME));
		}
	}

	private boolean hasDistributedTracing(Build build) {
		return build.dependencies().has(DISTRIBUTED_TRACING);
	}

	private boolean hasTracingProvider(Build build) {
		return hasZipkin(build) || build.dependencies().has("opentelemetry");
	}

	private boolean hasZipkin(Build build) {
		return build.dependencies().has("zipkin");
	}

	private boolean isBoot4OrLater() {
		return SPRING_BOOT_4_OR_LATER.match(this.bootVersion);
	}

}
