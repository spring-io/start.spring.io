/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.mongodb;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import io.spring.start.site.container.Testcontainers;
import io.spring.start.site.container.Testcontainers.Container;
import io.spring.start.site.container.Testcontainers.SupportedContainer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on MongoDB.
 *
 * @author Moritz Halbritter
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
@Configuration(proxyBeanMethods = false)
class MongoDbProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer mongoDbServiceConnectionsCustomizer(Build build, DockerServiceResolver serviceResolver,
			Testcontainers testcontainers) {
		return (serviceConnections) -> {
			if (isMongoEnabled(build)) {
				Container container = testcontainers.getContainer(SupportedContainer.MONGODB);
				serviceResolver.doWith("mongoDb", (service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("mongoDb", service, container.className(), container.generic())));
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
		return build.dependencies().has("mongodb") || build.dependencies().has("data-mongodb")
				|| build.dependencies().has("data-mongodb-reactive") || build.dependencies().has("batch-data-mongodb")
				|| build.dependencies().has("session-data-mongodb")
				|| build.dependencies().has("spring-ai-chat-memory-repository-mongodb");
	}

}
