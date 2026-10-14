/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.container;

import io.spring.initializr.generator.container.docker.compose.ComposeFile;

import org.springframework.core.Ordered;

/**
 * Callback for customizing a project's {@link ServiceConnections}. Invoked with an
 * {@link Ordered order} of {@code 0} by default, considering overriding
 * {@link #getOrder()} to customize this behaviour.
 *
 * @author Stephane Nicoll
 */
@FunctionalInterface
public interface ServiceConnectionsCustomizer extends Ordered {

	/**
	 * Customizes the given {@link ComposeFile}.
	 * @param serviceConnections the service connections to customize
	 */
	void customize(ServiceConnections serviceConnections);

	@Override
	default int getOrder() {
		return 0;
	}

}
