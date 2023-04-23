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

package io.spring.start.site.extension.dependency.testcontainers;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * Configuration for generation of projects that depend on Testcontainers.
 *
 * @author Eddú Meléndez
 */
class TestcontainersBuildCustomizer implements BuildCustomizer<Build> {

	private static final VersionRange SPRING_BOOT_3_1_0_OR_LATER = VersionParser.DEFAULT.parseRange("3.1.0-RC1");

	private final boolean isSpringBoot31OrLater;

	TestcontainersBuildCustomizer(InitializrMetadata metadata, ProjectDescription description) {
		this.isSpringBoot31OrLater = SPRING_BOOT_3_1_0_OR_LATER.match(description.getPlatformVersion());
	}

	@Override
	public void customize(Build build) {
		if (this.isSpringBoot31OrLater) {
			build.dependencies()
				.add("spring-boot-testcontainers",
						Dependency.withCoordinates("org.springframework.boot", "spring-boot-testcontainers")
							.scope(DependencyScope.TEST_COMPILE));
			build.dependencies().add("testcontainers");
		}
	}

}
