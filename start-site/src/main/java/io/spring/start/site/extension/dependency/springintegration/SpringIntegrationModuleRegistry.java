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
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;

/**
 * A registry of available {@link SpringIntegrationModule modules}.
 *
 * @author Artem  Bilan
 * @author Stephane Nicoll
 */
public final class SpringIntegrationModuleRegistry {

	private final List<SpringIntegrationModule> modules;

	private SpringIntegrationModuleRegistry(List<SpringIntegrationModule> modules) {
		this.modules = modules;
	}

	static SpringIntegrationModuleRegistry create(SpringIntegrationModule... modules) {
		return new SpringIntegrationModuleRegistry(Arrays.asList(modules));
	}

	static SpringIntegrationModuleRegistry create() {
		return create(
				moduleFor("AMQP Module", "amqp", "amqp"),
				moduleFor("Apache Geode Module", "gemfire", "geode"),
				moduleFor("HTTP Module", "http", "web"),
				moduleFor("JDBC Module", "jdbc", "jdbc"),
				moduleFor("JPA Module", "jpa", "data-jpa"),
				moduleFor("JMS Module", "jms", "activemq", "artemis"),
				moduleFor("Apache Kafka Module", "kafka", "kafka"),
				moduleFor("Mail Module", "mail", "mail"),
				moduleFor("MongoDB Module", "mongodb", "data-mongodb", "data-mongodb-reactive"),
				moduleFor("R2DBC Module", "r2dbc", "data-r2dbc"),
				moduleFor("Redis Module", "redis", "data-redis", "data-redis-reactive"),
				moduleFor("RSocket Module", "rsocket", "rsocket"),
				moduleFor("STOMP Module", "stomp", "websocket"),
				moduleFor("WebSocket Module", "websocket", "websocket"),
				moduleFor("WebFlux Module", "webflux", "webflux"),
				moduleFor("Security Module", "security", "security"),
				moduleFor("Web Services Module", "ws", "web-services"));
	}

	private static SpringIntegrationModule moduleFor(String name, String id, String... triggerDependencyIds) {
		return new SpringIntegrationModule(name, referenceLink(id), addDependency(id), triggerDependencyIds);
	}

	private static Consumer<Build> addDependency(String id) {
		String module = "spring-integration-" + id;
		return (build) -> build.dependencies()
				.add(module, Dependency.withCoordinates("org.springframework.integration", module)
						.scope(DependencyScope.COMPILE));
	}

	private static String referenceLink(String href) {
		return String.format("https://docs.spring.io/spring-integration/reference/html/%s.html", href);
	}

	Stream<SpringIntegrationModule> modules() {
		return this.modules.stream();
	}

}
