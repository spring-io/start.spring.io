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
		return build.dependencies().has("data-cassandra") || build.dependencies().has("data-cassandra-reactive")
				|| build.dependencies().has("spring-ai-vectordb-cassandra")
				|| build.dependencies().has("spring-ai-chat-memory-repository-cassandra");
	}

}
