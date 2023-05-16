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

package io.spring.start.site.extension.dependency.cassandra;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Cassandra.
 *
 * @author Moritz Halbritter
 * @author Stephane Nicoll
 */
@Configuration(proxyBeanMethods = false)
class CassandraProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CLASS_NAME = "org.testcontainers.containers.CassandraContainer";

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer cassandraServiceConnectionsCustomizer(Build build,
			DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> {
			if (isCassandraEnabled(build)) {
				serviceResolver.doWith("cassandra", (service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("cassandra", service, TESTCONTAINERS_CLASS_NAME)));
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
		return build.dependencies().has("data-cassandra") || build.dependencies().has("data-cassandra-reactive");
	}

}
