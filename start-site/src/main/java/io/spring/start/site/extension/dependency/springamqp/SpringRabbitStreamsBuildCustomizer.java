/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springamqp;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Adds AMQP if RabbitMQ Streams is selected.
 *
 * @author Moritz Halbritter
 */
class SpringRabbitStreamsBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		build.dependencies().add("rabbitmq");
	}

}
