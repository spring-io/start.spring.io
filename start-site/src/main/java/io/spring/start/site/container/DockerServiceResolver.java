/*
 * Copyright 2012-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
