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

	/**
	 * A single additional annotation to emit on the generated container method, and an
	 * optional callback to customize the {@link Annotation.Builder}.
	 *
	 * @param className the fully qualified annotation to add
	 * @param customizer the attribute configuration, or {@code null} to apply no
	 * attributes
	 */
	public record AnnotationRequest(ClassName className, Consumer<Annotation.Builder> customizer) {
		public AnnotationRequest {
			customizer = (customizer != null) ? customizer : (builder) -> {
			};
		}
	}

	/**
	 * A {@code @ServiceConnection} backed by a Testcontainers image and container class,
	 * plus any extra method-level annotations to generate.
	 *
	 * @param id unique id for the connection
	 * @param dockerService image, ports, and other compose metadata
	 * @param containerClassName the container class to construct (or
	 * {@link Testcontainers#GENERIC_CONTAINER_CLASS_NAME} for generic container support)
	 * @param containerClassNameGeneric if the return type and constructor use a type
	 * parameter
	 * @param connectionName the value for the {@code name} attribute of
	 * {@code @ServiceConnection} when not {@code null}
	 * @param annotations additional annotations to place on the generated method after
	 * {@code @Bean} and {@code @ServiceConnection}
	 */
	public record ServiceConnection(String id, DockerService dockerService, String containerClassName,
			boolean containerClassNameGeneric, String connectionName, List<AnnotationRequest> annotations) {

		public ServiceConnection {
			annotations = (annotations != null) ? List.copyOf(annotations) : List.of();
		}

		/**
		 * Whether this connection uses
		 * {@value Testcontainers#GENERIC_CONTAINER_CLASS_NAME} (see
		 * {@link Testcontainers}) rather than a specific container class.
		 * @return {@code true} for generic container usage
		 */
		public boolean isGenericContainer() {
			return this.containerClassName.equals(Testcontainers.GENERIC_CONTAINER_CLASS_NAME);
		}

		/**
		 * Add an annotation on the generated container method, with no attributes.
		 * Returns a new instance; this record is not modified.
		 * @param annotationClassName the annotation to add
		 * @return a new {@link ServiceConnection} with the additional annotation
		 * @see #withAnnotation(ClassName, Consumer)
		 */
		public ServiceConnection withAnnotation(ClassName annotationClassName) {
			return withAnnotation(annotationClassName, (builder) -> {
			});
		}

		/**
		 * Add an annotation on the generated container method, configuring attributes on
		 * {@link Annotation.Builder}. Returns a new instance; this record is not
		 * modified.
		 * @param annotationClassName the annotation to add
		 * @param customizer attribute configuration, or {@code null} to apply no
		 * attributes
		 * @return a new {@link ServiceConnection} with the additional annotation
		 */
		public ServiceConnection withAnnotation(ClassName annotationClassName,
				Consumer<Annotation.Builder> customizer) {
			List<AnnotationRequest> newAnnotations = new ArrayList<>(this.annotations);
			newAnnotations.add(new AnnotationRequest(annotationClassName, customizer));
			return new ServiceConnection(this.id, this.dockerService, this.containerClassName,
					this.containerClassNameGeneric, this.connectionName, newAnnotations);
		}

		/**
		 * Create a service connection for a generic Testcontainers container (see
		 * {@value Testcontainers#GENERIC_CONTAINER_CLASS_NAME}) and set the
		 * {@code @ServiceConnection#name} attribute to {@code connectionName}.
		 * @param id unique id for the connection
		 * @param dockerService image, ports, and other compose metadata
		 * @param connectionName the {@code name} for {@code @ServiceConnection}
		 * @return a new {@link ServiceConnection}
		 */
		public static ServiceConnection ofGenericContainer(String id, DockerService dockerService,
				String connectionName) {
			return new ServiceConnection(id, dockerService, Testcontainers.GENERIC_CONTAINER_CLASS_NAME, true,
					connectionName, List.of());
		}

		/**
		 * Create a service connection for a typed Testcontainers class (no
		 * {@code @ServiceConnection#name} attribute).
		 * @param id unique id for the connection
		 * @param dockerService image, ports, and other compose metadata
		 * @param containerClassName the container class to construct
		 * @param containerClassNameGeneric if the return type and constructor use a type
		 * parameter
		 * @return a new {@link ServiceConnection}
		 */
		public static ServiceConnection ofContainer(String id, DockerService dockerService, String containerClassName,
				boolean containerClassNameGeneric) {
			return new ServiceConnection(id, dockerService, containerClassName, containerClassNameGeneric, null,
					List.of());
		}
	}

}
