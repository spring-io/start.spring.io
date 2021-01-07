/*
 * Copyright 2012-2021 the original author or authors.
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

package io.spring.start.site.extension.dependency.springintegration;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependency.Builder;

/**
 * A registry of available Spring Integration modules.
 *
 * @author Artem Bilan
 * @author Stephane Nicoll
 */
abstract class SpringIntegrationModuleRegistry {

	static Iterable<ImplicitDependency> create() {
		return create(
				onDependencies("activemq", "artemis").customizeBuild(addDependency("jms"))
						.customizeHelpDocument(addReferenceLink("JMS Module", "jms")),
				onDependencies("amqp").customizeBuild(addDependency("amqp"))
						.customizeHelpDocument(addReferenceLink("AMQP Module", "amqp")),
				onDependencies("data-jdbc", "jdbc").customizeBuild(addDependency("jdbc"))
						.customizeHelpDocument(addReferenceLink("JDBC Module", "jdbc")),
				onDependencies("data-jpa").customizeBuild(addDependency("jpa"))
						.customizeHelpDocument(addReferenceLink("JPA Module", "jpa")),
				onDependencies("data-mongodb", "data-mongodb-reactive").customizeBuild(addDependency("mongodb"))
						.customizeHelpDocument(addReferenceLink("MongoDB Module", "mongodb")),
				onDependencies("data-r2dbc").customizeBuild(addDependency("r2dbc"))
						.customizeHelpDocument(addReferenceLink("R2DBC Module", "r2dbc")),
				onDependencies("data-redis", "data-redis-reactive").customizeBuild(addDependency("redis"))
						.customizeHelpDocument(addReferenceLink("Redis Module", "redis")),
				onDependencies("geode").customizeBuild(addDependency("gemfire"))
						.customizeHelpDocument(addReferenceLink("Apache Geode Module", "gemfire")),
				onDependencies("integration").customizeBuild(addDependency("test", DependencyScope.TEST_COMPILE))
						.customizeHelpDocument(addReferenceLink("Test Module", "testing")),
				onDependencies("kafka", "kafka-streams").customizeBuild(addDependency("kafka"))
						.customizeHelpDocument(addReferenceLink("Apache Kafka Module", "kafka")),
				onDependencies("mail").customizeBuild(addDependency("mail"))
						.customizeHelpDocument(addReferenceLink("Mail Module", "mail")),
				onDependencies("rsocket").customizeBuild(addDependency("rsocket"))
						.customizeHelpDocument(addReferenceLink("RSocket Module", "rsocket")),
				onDependencies("security").customizeBuild(addDependency("security"))
						.customizeHelpDocument(addReferenceLink("Security Module", "security")),
				onDependencies("web").customizeBuild(addDependency("http"))
						.customizeHelpDocument(addReferenceLink("HTTP Module", "http")),
				onDependencies("webflux").customizeBuild(addDependency("webflux"))
						.customizeHelpDocument(addReferenceLink("WebFlux Module", "webflux")),
				onDependencies("websocket").customizeBuild(addDependency("stomp").andThen(addDependency("websocket")))
						.customizeHelpDocument(addReferenceLink("STOMP Module", "stomp")
								.andThen(addReferenceLink("WebSocket Module", "websocket"))),
				onDependencies("web-services").customizeBuild(addDependency("ws"))
						.customizeHelpDocument(addReferenceLink("Web Services Module", "ws")));
	}

	private static Iterable<ImplicitDependency> create(ImplicitDependency.Builder... dependencies) {
		return Arrays.stream(dependencies).map(Builder::build).collect(Collectors.toList());
	}

	private static ImplicitDependency.Builder onDependencies(String... dependencyIds) {
		return new Builder().matchAnyDependencyIds(dependencyIds);
	}

	private static Consumer<Build> addDependency(String id) {
		return addDependency(id, DependencyScope.COMPILE);
	}

	private static Consumer<Build> addDependency(String id, DependencyScope scope) {
		return (build) -> build.dependencies().add("integration-" + id,
				Dependency.withCoordinates("org.springframework.integration", "spring-integration-" + id).scope(scope));
	}

	private static Consumer<HelpDocument> addReferenceLink(String name, String id) {
		return (helpDocument) -> {
			String href = String.format("https://docs.spring.io/spring-integration/reference/html/%s.html", id);
			String description = String.format("Spring Integration %s Reference Guide", name);
			helpDocument.gettingStarted().addReferenceDocLink(href, description);
		};
	}

}
