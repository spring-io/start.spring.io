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

package io.spring.start.site.extension.dependency.reactor;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.build.BuildMetadataResolver;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * A {@link BuildCustomizer} that automatically adds "reactor-test" when a dependency with
 * the {@code reactive} facet is selected.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 * @author Moritz Halbritter
 */
public class ReactorTestBuildCustomizer implements BuildCustomizer<Build> {

	private static final VersionRange SPRING_BOOT_4_0_RC2_OR_LATER = VersionParser.DEFAULT.parseRange("4.0.0-RC2");

	private final BuildMetadataResolver buildResolver;

	private final Version bootVersion;

	public ReactorTestBuildCustomizer(InitializrMetadata metadata, ProjectDescription description) {
		this.bootVersion = description.getPlatformVersion();
		this.buildResolver = new BuildMetadataResolver(metadata, description.getPlatformVersion());
	}

	@Override
	public void customize(Build build) {
		if (shouldAddReactorTestDependency(build)) {
			build.dependencies()
				.add("reactor-test", Dependency.withCoordinates("io.projectreactor", "reactor-test")
					.scope(DependencyScope.TEST_COMPILE));
		}
	}

	private boolean shouldAddReactorTestDependency(Build build) {
		if (SPRING_BOOT_4_0_RC2_OR_LATER.match(this.bootVersion)) {
			// Starting with Boot 4.0.0-RC2, all Spring Boot reactive starters have a
			// dependency on io.projectreactor:reactor-test in their test starter
			return this.buildResolver.dependencies(build)
				.anyMatch((dependency) -> hasReactiveFacet(dependency) && !isSpringBootStarter(dependency));
		}
		return this.buildResolver.hasFacet(build, "reactive");
	}

	private boolean hasReactiveFacet(io.spring.initializr.metadata.Dependency dependency) {
		return dependency.getFacets().contains("reactive");
	}

	private boolean isSpringBootStarter(io.spring.initializr.metadata.Dependency dependency) {
		return dependency.isStarter() && dependency.getGroupId().equals("org.springframework.boot");
	}

}
