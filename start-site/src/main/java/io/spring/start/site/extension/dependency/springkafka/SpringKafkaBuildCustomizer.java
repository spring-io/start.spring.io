/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.dependency.springkafka;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionProperty;
import io.spring.initializr.generator.version.VersionRange;

/**
 * A {@link BuildCustomizer} that automatically adds "spring-kafka-test" when kafka is
 * selected and overrides to a more stable version when using Spring Boot 1.x.
 *
 * @author Wonwoo Lee
 * @author Stephane Nicoll
 * @author Madhura Bhave
 */
public class SpringKafkaBuildCustomizer implements BuildCustomizer<Build> {

	private static final VersionRange SPRING_BOOT_2_0_OR_LATER = VersionParser.DEFAULT.parseRange("2.0.0.M1");

	private final ProjectDescription description;

	public SpringKafkaBuildCustomizer(ProjectDescription description) {
		this.description = description;
	}

	@Override
	public void customize(Build build) {
		if (build.dependencies().has("kafka")) {
			build.dependencies().add("spring-kafka-test",
					Dependency.withCoordinates("org.springframework.kafka", "spring-kafka-test")
							.scope(DependencyScope.TEST_COMPILE));
			// Override to a more recent version
			if (!SPRING_BOOT_2_0_OR_LATER.match(this.description.getPlatformVersion())) {
				build.properties().version(VersionProperty.of("spring-kafka.version", false), "1.3.10.RELEASE");
			}
		}
	}

}
