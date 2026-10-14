/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springsecurity;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that provides Spring Security's RSocket support when both
 * RSocket and Spring Security are selected.
 *
 * @author Stephane Nicoll
 */
public class SpringSecurityRSocketBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (build.dependencies().has("rsocket")) {
			build.dependencies()
				.add("security-rsocket",
						Dependency.withCoordinates("org.springframework.security", "spring-security-rsocket"));
			build.dependencies()
				.add("security-messaging",
						Dependency.withCoordinates("org.springframework.security", "spring-security-messaging"));
		}
	}

}
