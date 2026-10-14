/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.container;

import io.spring.initializr.generator.container.docker.compose.ComposeFile;

import org.springframework.core.Ordered;

/**
 * Callback for customizing a project's {@link ComposeFile}. Invoked with an
 * {@link Ordered order} of {@code 0} by default, considering overriding
 * {@link #getOrder()} to customize this behaviour.
 *
 * @author Moritz Halbritter
 */
@FunctionalInterface
public interface ComposeFileCustomizer extends Ordered {

	/**
	 * Customizes the given {@link ComposeFile}.
	 * @param composeFile the compose file to customize
	 */
	void customize(ComposeFile composeFile);

	@Override
	default int getOrder() {
		return 0;
	}

}
