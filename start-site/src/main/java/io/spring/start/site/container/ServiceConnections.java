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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Container for {@link ServiceConnection}.
 *
 * @author Stephane Nicoll
 */
public class ServiceConnections {

	private static final String GENERIC_CONTAINER_CLASS_NAME = "org.testcontainers.containers.GenericContainer";

	private final Map<String, ServiceConnection> connectionsById = new HashMap<>();

	public void addServiceConnection(ServiceConnection serviceConnection) {
		String id = serviceConnection.id();
		if (this.connectionsById.containsKey(id)) {
			throw new IllegalArgumentException("Connection with id '" + id + "' already registered");
		}
		this.connectionsById.put(id, serviceConnection);
	}

	public Stream<ServiceConnection> values() {
		return this.connectionsById.values().stream().sorted(Comparator.comparing(ServiceConnection::id));
	}

	public record ServiceConnection(String id, DockerService dockerService, String containerClassName,
			boolean containerClassNameGeneric, String connectionName) {

		public static ServiceConnection ofGenericContainer(String id, DockerService dockerService,
				String connectionName) {
			return new ServiceConnection(id, dockerService, GENERIC_CONTAINER_CLASS_NAME, true, connectionName);
		}

		public static ServiceConnection ofContainer(String id, DockerService dockerService, String containerClassName) {
			return ofContainer(id, dockerService, containerClassName, true);
		}

		public static ServiceConnection ofContainer(String id, DockerService dockerService, String containerClassName,
				boolean containerClassNameGeneric) {
			return new ServiceConnection(id, dockerService, containerClassName, containerClassNameGeneric, null);
		}

		public boolean isGenericContainer() {
			return this.containerClassName.equals(GENERIC_CONTAINER_CLASS_NAME);
		}

	}

}
