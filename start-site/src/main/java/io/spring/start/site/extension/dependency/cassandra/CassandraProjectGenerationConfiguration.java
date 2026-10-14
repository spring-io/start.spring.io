/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.cassandra;

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
 * Configuration for generation of projects that depend on Cassandra.
 *
 * @author Moritz Halbritter
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
@Configuration(proxyBeanMethods = false)
class CassandraProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer cassandraServiceConnectionsCustomizer(Build build,
			DockerServiceResolver serviceResolver, Testcontainers testcontainers) {
		return (serviceConnections) -> {
			if (isCassandraEnabled(build)) {
				Container container = testcontainers.getContainer(SupportedContainer.CASSANDRA);
				serviceResolver.doWith("cassandra",
						(service) -> serviceConnections.addServiceConnection(ServiceConnection.ofContainer("cassandra",
								service, container.className(), container.generic())));
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer cassandraComposeFileCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (composeFile) -> {
			if (isCassandraEnabled(build)) {
				serviceResolver.doWith("cassandra",
						(service) -> composeFile.services()
							.add("cassandra", service.andThen((builder) -> builder.environment("CASSANDRA_DC", "dc1")
								.environment("CASSANDRA_ENDPOINT_SNITCH", "GossipingPropertyFileSnitch"))));
			}
		};
	}

	private boolean isCassandraEnabled(Build build) {
		return build.dependencies().has("cassandra") || build.dependencies().has("data-cassandra")
				|| build.dependencies().has("data-cassandra-reactive")
				|| build.dependencies().has("spring-ai-vectordb-cassandra")
				|| build.dependencies().has("spring-ai-chat-memory-repository-cassandra");
	}

}
