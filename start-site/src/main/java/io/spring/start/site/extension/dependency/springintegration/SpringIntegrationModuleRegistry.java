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

package io.spring.start.site.extension.dependency.springintegration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.version.Version;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependency.Builder;

/**
 * A registry of available Spring Integration modules.
 *
 * @author Artem Bilan
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 * @author Eddú Meléndez
 */
abstract class SpringIntegrationModuleRegistry {

	static Iterable<ImplicitDependency> create(Version platformVersion) {
		List<Builder> builders = new ArrayList<>();
		builders.add(onDependencies("activemq", "artemis").customizeBuild(addDependency("jms"))
			.customizeHelpDocument(addReferenceLink("JMS Module", "jms")));
		builders.add(onDependencies("amqp", "amqp-streams").customizeBuild(addDependency("amqp"))
			.customizeHelpDocument(addReferenceLink("AMQP Module", "amqp")));
		builders.add(onDependencies("data-jdbc", "jdbc").customizeBuild(addDependency("jdbc"))
			.customizeHelpDocument(addReferenceLink("JDBC Module", "jdbc")));
		builders.add(onDependencies("data-jpa").customizeBuild(addDependency("jpa"))
			.customizeHelpDocument(addReferenceLink("JPA Module", "jpa")));
		builders.add(onDependencies("data-mongodb", "data-mongodb-reactive").customizeBuild(addDependency("mongodb"))
			.customizeHelpDocument(addReferenceLink("MongoDB Module", "mongodb")));
		builders.add(onDependencies("data-r2dbc").customizeBuild(addDependency("r2dbc"))
			.customizeHelpDocument(addReferenceLink("R2DBC Module", "r2dbc")));
		builders.add(onDependencies("data-redis", "data-redis-reactive").customizeBuild(addDependency("redis"))
			.customizeHelpDocument(addReferenceLink("Redis Module", "redis")));
		builders.add(onDependencies("integration").customizeBuild(addDependency("test", DependencyScope.TEST_COMPILE))
			.customizeHelpDocument(addReferenceLink("Test Module", "testing")));
		builders.add(onDependencies("kafka", "kafka-streams").customizeBuild(addDependency("kafka"))
			.customizeHelpDocument(addReferenceLink("Apache Kafka Module", "kafka")));
		builders.add(onDependencies("mail").customizeBuild(addDependency("mail"))
			.customizeHelpDocument(addReferenceLink("Mail Module", "mail")));
		builders.add(onDependencies("rsocket").customizeBuild(addDependency("rsocket"))
			.customizeHelpDocument(addReferenceLink("RSocket Module", "rsocket")));
		builders.add(onDependencies("security")
			.customizeBuild(addDependency("spring-security-messaging", "org.springframework.security",
					"spring-security-messaging", DependencyScope.COMPILE))
			.customizeHelpDocument(addReferenceLink("Security Module", "security")));
		builders.add(onDependencies("web").customizeBuild(addDependency("http"))
			.customizeHelpDocument(addReferenceLink("HTTP Module", "http")));
		builders.add(onDependencies("webflux").customizeBuild(addDependency("webflux"))
			.customizeHelpDocument(addReferenceLink("WebFlux Module", "webflux")));
		builders
			.add(onDependencies("websocket").customizeBuild(addDependency("stomp").andThen(addDependency("websocket")))
				.customizeHelpDocument(addReferenceLink("STOMP Module", "stomp")
					.andThen(addReferenceLink("WebSocket Module", "web-sockets"))));
		builders.add(onDependencies("web-services").customizeBuild(addDependency("ws"))
			.customizeHelpDocument(addReferenceLink("Web Services Module", "ws")));
		return builders.stream().map(Builder::build).toList();
	}

	private static ImplicitDependency.Builder onDependencies(String... dependencyIds) {
		return new Builder().matchAnyDependencyIds(dependencyIds);
	}

	private static Consumer<Build> addDependency(String id) {
		return addDependency(id, DependencyScope.COMPILE);
	}

	private static Consumer<Build> addDependency(String id, DependencyScope scope) {
		return addDependency("integration-" + id, "org.springframework.integration", "spring-integration-" + id, scope);
	}

	private static Consumer<Build> addDependency(String id, String groupId, String artifactId, DependencyScope scope) {
		return (build) -> build.dependencies().add(id, Dependency.withCoordinates(groupId, artifactId).scope(scope));
	}

	private static Consumer<HelpDocument> addReferenceLink(String name, String id) {
		return (helpDocument) -> {
			String href = "https://docs.spring.io/spring-integration/reference/%s.html".formatted(id);
			String description = String.format("Spring Integration %s Reference Guide", name);
			helpDocument.gettingStarted().addReferenceDocLink(href, description);
		};
	}

}
