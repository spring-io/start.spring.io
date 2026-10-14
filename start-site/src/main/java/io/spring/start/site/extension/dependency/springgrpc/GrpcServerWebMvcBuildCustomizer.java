/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to replace 'spring-grpc-server-spring-boot-starter' with
 * 'spring-grpc-server-web-spring-boot-starter' if WebMVC is selected.
 *
 * @author Moritz Halbritter
 */
class GrpcServerWebMvcBuildCustomizer implements BuildCustomizer<Build> {

	private static final String DEPENDENCY_ID = "spring-grpc-server";

	@Override
	public void customize(Build build) {
		Dependency dependency = build.dependencies().get(DEPENDENCY_ID);
		if (dependency == null) {
			return;
		}
		build.dependencies().remove(DEPENDENCY_ID);
		build.dependencies()
			.add(DEPENDENCY_ID, dependency.getGroupId(), "spring-grpc-server-web-spring-boot-starter",
					dependency.getScope());
	}

}
