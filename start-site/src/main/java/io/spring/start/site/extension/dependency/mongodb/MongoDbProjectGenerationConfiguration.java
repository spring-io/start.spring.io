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

package io.spring.start.site.extension.dependency.mongodb;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on MongoDB.
 *
 * @author Moritz Halbritter
 * @author Stephane Nicoll
 */
@Configuration(proxyBeanMethods = false)
class MongoDbProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CLASS_NAME = "org.testcontainers.containers.MongoDBContainer";

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer mongoDbServiceConnectionsCustomizer(Build build,
			DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> {
			if (isMongoEnabled(build)) {
				serviceResolver.doWith("mongoDb", (service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("mongoDb", service, TESTCONTAINERS_CLASS_NAME, false)));
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer mongoDbComposeFileCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (composeFile) -> {
			if (isMongoEnabled(build)) {
				serviceResolver.doWith("mongoDb", (service) -> composeFile.services()
					.add("mongodb",
							service.andThen((builder) -> builder.environment("MONGO_INITDB_ROOT_USERNAME", "root")
								.environment("MONGO_INITDB_ROOT_PASSWORD", "secret")
								.environment("MONGO_INITDB_DATABASE", "mydatabase"))));
			}
		};
	}

	private boolean isMongoEnabled(Build build) {
		return build.dependencies().has("data-mongodb") || build.dependencies().has("data-mongodb-reactive");
	}

}
