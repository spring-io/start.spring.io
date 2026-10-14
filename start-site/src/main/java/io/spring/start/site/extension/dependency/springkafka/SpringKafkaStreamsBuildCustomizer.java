/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springkafka;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Adds a dependency to Kafka.
 *
 * @author Moritz Halbritter
 */
class SpringKafkaStreamsBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		build.dependencies().add("kafka");
	}

}
