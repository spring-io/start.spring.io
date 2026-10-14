/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to add additional dependencies for Spring gRPC.
 *
 * @author Moritz Halbritter
 */
class GrpcAdditionalDependenciesBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		build.dependencies().add("grpc-services", "io.grpc", "grpc-services", DependencyScope.COMPILE);
		build.dependencies()
			.add("spring-grpc-test", "org.springframework.grpc", "spring-grpc-test", DependencyScope.TEST_COMPILE);
	}

}
