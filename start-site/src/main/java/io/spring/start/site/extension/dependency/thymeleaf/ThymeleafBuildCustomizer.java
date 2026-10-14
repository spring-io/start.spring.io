/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.thymeleaf;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} for Thymeleaf.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
public class ThymeleafBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (build.dependencies().has("security") || build.dependencies().has("oauth2-client")) {
			build.dependencies()
				.add("thymeleaf-extras-spring-security",
						Dependency.withCoordinates("org.thymeleaf.extras", "thymeleaf-extras-springsecurity6"));
		}
	}

}
