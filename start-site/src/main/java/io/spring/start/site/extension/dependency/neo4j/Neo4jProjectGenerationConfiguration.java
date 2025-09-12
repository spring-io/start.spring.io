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

package io.spring.start.site.extension.dependency.neo4j;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Neo4j.
 *
 * @author Eddú Meléndez
 */
@Configuration(proxyBeanMethods = false)
public class Neo4jProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CLASS_NAME = "org.testcontainers.containers.Neo4jContainer";

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer neo4jServiceConnectionsCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> {
			if (isNeo4jEnabled(build)) {
				serviceResolver.doWith("neo4j", (service) -> serviceConnections
					.addServiceConnection(ServiceConnection.ofContainer("neo4j", service, TESTCONTAINERS_CLASS_NAME)));
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer neo4jComposeFileCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (composeFile) -> {
			if (isNeo4jEnabled(build)) {
				serviceResolver.doWith("neo4j", (service) -> composeFile.services()
					.add("neo4j",
							service.andThen((builder) -> builder.environment("NEO4J_AUTH", "neo4j/notverysecret"))));
			}
		};
	}

	private boolean isNeo4jEnabled(Build build) {
		return build.dependencies().has("data-neo4j") || build.dependencies().has("spring-ai-vectordb-neo4j");
	}

}
