/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.container;

import java.util.function.Consumer;

/**
 * Resolve docker services against their identifiers.
 *
 * @author Stephane Nicoll
 */
public interface DockerServiceResolver {

	/**
	 * Resolve the {@link DockerService} with the specified {@code id}.
	 * @param id the id of the docker service
	 * @return the docker service with that id or {@code null}
	 */
	DockerService resolve(String id);

	/**
	 * Do something with a {@link DockerService} with the specified {@code id}. If no such
	 * service exists, do nothing.
	 * @param id the id of the service
	 * @param service a consumer of the service
	 */
	default void doWith(String id, Consumer<DockerService> service) {
		DockerService dockerService = resolve(id);
		if (dockerService != null) {
			service.accept(dockerService);
		}
	}

}
