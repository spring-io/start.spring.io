/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency;

import java.util.List;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} which adds the according test starter if a spring boot starter
 * is selected.
 *
 * @author Moritz Halbritter
 */
class AddTestStartersBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		List<String> ids = build.dependencies().ids().toList();
		for (String id : ids) {
			Dependency dependency = build.dependencies().get(id);
			if (isBootStarter(dependency)) {
				build.dependencies()
					.add(id + "-test", dependency.getGroupId(), dependency.getArtifactId() + "-test",
							DependencyScope.TEST_COMPILE);
			}
		}
	}

	private boolean isBootStarter(Dependency dependency) {
		return dependency.getGroupId().equals("org.springframework.boot")
				&& dependency.getArtifactId().startsWith("spring-boot-starter")
				&& DependencyScope.COMPILE.equals(dependency.getScope());
	}

}
