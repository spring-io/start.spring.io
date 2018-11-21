/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionProperty;

/**
 * A {@link BuildCustomizer} that automatically adds "spring-kafka-test" when kafka is
 * selected and overrides to a more stable version when using Spring Boot 1.x.
 *
 * @author Wonwoo Lee
 * @author Stephane Nicoll
 * @author Madhura Bhave
 */
public class SpringKafkaBuildCustomizer implements BuildCustomizer<Build> {

	private static final Version VERSION_2_0_0 = Version.parse("2.0.0.M1");

	private final ResolvedProjectDescription description;

	public SpringKafkaBuildCustomizer(ResolvedProjectDescription description) {
		this.description = description;
	}

	@Override
	public void customize(Build build) {
		if (hasDependency("kafka", build)) {
			build.dependencies().add("spring-kafka-test", "org.springframework.kafka",
					"spring-kafka-test", null, DependencyScope.TEST_COMPILE);
			// Override to a more recent version
			if (isSpringBootVersionBefore()) {
				build.addVersionProperty(
						VersionProperty.of("spring-kafka.version", false),
						"1.3.8.RELEASE");
			}
		}
	}

	protected boolean hasDependency(String id, Build build) {
		return build.dependencies().ids().anyMatch(i -> i.equals(id));
	}

	protected boolean isSpringBootVersionBefore() {
		return (VERSION_2_0_0.compareTo(this.description.getPlatformVersion()) > 0);
	}

}
