/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Determine the appropriate Spring Cloud stream dependency to use based on the selected
 * integration technology.
 * <p>
 * Does not replace the integration technology jar by the relevant binder. If more than
 * one tech is selected, it is far easier to remove the unnecessary binder jar than to
 * figure out the name of the tech jar to add to keep support for that technology.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 * @author Brian Clozel
 */
class SpringCloudStreamBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (hasDependency("cloud-stream", build) || hasDependency("cloud-bus", build)) {
			if (hasDependency("rabbitmq", build)) {
				build.dependencies()
					.add("cloud-stream-binder-rabbit", "org.springframework.cloud", "spring-cloud-stream-binder-rabbit",
							DependencyScope.COMPILE);
			}
			if (hasDependency("kafka", build)) {
				build.dependencies()
					.add("cloud-stream-binder-kafka", "org.springframework.cloud", "spring-cloud-stream-binder-kafka",
							DependencyScope.COMPILE);
			}
			if (hasDependency("pulsar", build)) {
				build.dependencies()
					.add("cloud-stream-binder-pulsar", "org.springframework.cloud", "spring-cloud-stream-binder-pulsar",
							DependencyScope.COMPILE);
			}
		}
		// Spring Cloud Stream specific
		if (hasDependency("cloud-stream", build)) {
			if (hasDependency("kafka-streams", build)) {
				build.dependencies()
					.add("cloud-stream-binder-kafka-streams", "org.springframework.cloud",
							"spring-cloud-stream-binder-kafka-streams", DependencyScope.COMPILE);
			}
			build.dependencies()
				.add("cloud-stream-test",
						Dependency.withCoordinates("org.springframework.cloud", "spring-cloud-stream-test-binder")
							.scope(DependencyScope.TEST_COMPILE));
		}
	}

	protected boolean hasDependency(String id, Build build) {
		return build.dependencies().has(id);
	}

}
