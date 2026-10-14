/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.reactor;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.build.BuildMetadataResolver;
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

	private final BuildMetadataResolver buildResolver;

	public ReactorTestBuildCustomizer(InitializrMetadata metadata, ProjectDescription description) {
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
		return this.buildResolver.dependencies(build)
			.anyMatch((dependency) -> hasReactiveFacet(dependency) && !isSpringBootStarter(dependency));
	}

	private boolean hasReactiveFacet(io.spring.initializr.metadata.Dependency dependency) {
		return dependency.getFacets().contains("reactive");
	}

	private boolean isSpringBootStarter(io.spring.initializr.metadata.Dependency dependency) {
		return dependency.isStarter() && dependency.getGroupId().equals("org.springframework.boot");
	}

}
