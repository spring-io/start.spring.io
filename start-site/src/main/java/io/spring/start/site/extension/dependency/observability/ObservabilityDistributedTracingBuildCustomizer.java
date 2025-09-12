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

/**
 * Configures distributed tracing if necessary.
 *
 * @author Stephane Nicoll
 */
class ObservabilityDistributedTracingBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		// Zipkin without distributed tracing make no sense
		if (build.dependencies().has("zipkin") && !build.dependencies().has("distributed-tracing")) {
			build.dependencies().add("distributed-tracing");
		}
		if (build.dependencies().has("wavefront") && build.dependencies().has("distributed-tracing")) {
			build.dependencies()
				.add("wavefront-tracing-reporter",
						Dependency.withCoordinates("io.micrometer", "micrometer-tracing-reporter-wavefront")
							.scope(DependencyScope.RUNTIME));
		}
	}

}
