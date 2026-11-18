/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that automatically adds
 * "graphql-dgs-spring-graphql-starter-test" when the {@code dgs} dependency is present.
 *
 * @author Brian Clozel
 */
class DgsBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		build.dependencies()
			.add("graphql-dgs-spring-graphql-starter-test",
					Dependency.withCoordinates("com.netflix.graphql.dgs", "graphql-dgs-spring-graphql-starter-test")
						.scope(DependencyScope.TEST_COMPILE));
	}

}
