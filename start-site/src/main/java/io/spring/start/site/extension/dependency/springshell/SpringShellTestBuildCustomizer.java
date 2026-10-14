/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springshell;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that automatically adds {@code spring-shell-test} when Spring
 * Shell is selected.
 *
 * @author DaShaun Carter
 */
public class SpringShellTestBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		build.dependencies()
			.add("spring-shell-starter-test",
					Dependency.withCoordinates("org.springframework.shell", "spring-shell-starter-test")
						.scope(DependencyScope.TEST_COMPILE));
	}

}
