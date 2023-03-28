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

package io.spring.start.site.extension.dependency.springpulsar;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionReference;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * A {@link BuildCustomizer} that automatically adds
 * {@code spring-pulsar-spring-cloud-stream-binder} when Pulsar and Spring Cloud Stream
 * are both selected.
 *
 * @author Chris Bono
 */
class SpringPulsarBinderBuildCustomizer implements BuildCustomizer<Build> {

	private final String pulsarVersion;

	SpringPulsarBinderBuildCustomizer(InitializrMetadata metadata, ProjectDescription description) {
		this.pulsarVersion = metadata.getDependencies()
			.get("pulsar")
			.resolve(description.getPlatformVersion())
			.getVersion();
	}

	@Override
	public void customize(Build build) {
		build.dependencies()
			.add("pulsar-binder",
					Dependency.withCoordinates("org.springframework.pulsar", "spring-pulsar-spring-cloud-stream-binder")
						.version(VersionReference.ofValue(this.pulsarVersion)));
	}

}
