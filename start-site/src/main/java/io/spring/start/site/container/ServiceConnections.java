/*
 * Copyright 2012 - present the original author or authors.
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.ClassName;

/**
 * Container for {@link ServiceConnection}.
 *
 * @author Stephane Nicoll
 * @author Kaique Vieira Soares
 */
public class ServiceConnections {

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

	public record AnnotationRequest(ClassName className, Consumer<Annotation.Builder> customizer) {
		public AnnotationRequest {
			customizer = (customizer != null) ? customizer : (builder) -> {
			};
		}
	}

	public record ServiceConnection(String id, DockerService dockerService, String containerClassName,
			boolean containerClassNameGeneric, String connectionName, List<AnnotationRequest> annotations) {

		public ServiceConnection {
			annotations = (annotations != null) ? List.copyOf(annotations) : List.of();
		}

		public static ServiceConnection ofGenericContainer(String id, DockerService dockerService,
				String connectionName) {
			return new ServiceConnection(id, dockerService, Testcontainers.GENERIC_CONTAINER_CLASS_NAME, true,
					connectionName, List.of());
		}

		public static ServiceConnection ofContainer(String id, DockerService dockerService, String containerClassName,
				boolean containerClassNameGeneric) {
			return new ServiceConnection(id, dockerService, containerClassName, containerClassNameGeneric, null,
					List.of());
		}

		public boolean isGenericContainer() {
			return this.containerClassName.equals(Testcontainers.GENERIC_CONTAINER_CLASS_NAME);
		}

		public ServiceConnection withAnnotation(ClassName annotationClassName) {
			return withAnnotation(annotationClassName, (builder) -> {
			});
		}

		public ServiceConnection withAnnotation(ClassName annotationClassName,
				Consumer<Annotation.Builder> customizer) {
			List<AnnotationRequest> newAnnotations = new ArrayList<>(this.annotations);
			newAnnotations.add(new AnnotationRequest(annotationClassName, customizer));
			return new ServiceConnection(this.id, this.dockerService, this.containerClassName,
					this.containerClassNameGeneric, this.connectionName, newAnnotations);
		}
	}

}
