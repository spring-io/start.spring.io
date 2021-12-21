/*
 * Copyright 2012-2021 the original author or authors.
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

package io.spring.start.site.extension.dependency.graphql;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.build.BuildMetadataResolver;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * A {@link BuildCustomizer} that automatically adds "spring-graphql-test" when the
 * {@code graphql} dependency. Additionnally, a {@code spring-webflux} dependency is added
 * in the test scope if not already present.
 *
 * @author Brian Clozel
 */
public class SpringGraphQlBuildCustomizer implements BuildCustomizer<Build> {

	private final BuildMetadataResolver buildResolver;

	public SpringGraphQlBuildCustomizer(InitializrMetadata metadata) {
		this.buildResolver = new BuildMetadataResolver(metadata);
	}

	@Override
	public void customize(Build build) {
		build.dependencies().add("spring-graphql-test",
				Dependency.withCoordinates("org.springframework.graphql", "spring-graphql-test")
						.scope(DependencyScope.TEST_COMPILE));
		if (!this.buildResolver.hasFacet(build, "reactive")) {
			build.dependencies().add("spring-webflux", Dependency
					.withCoordinates("org.springframework", "spring-webflux").scope(DependencyScope.TEST_COMPILE));
		}
	}

}
