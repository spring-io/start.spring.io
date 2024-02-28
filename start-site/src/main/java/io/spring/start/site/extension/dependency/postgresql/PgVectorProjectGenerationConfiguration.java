/*
 * Copyright 2012-2024 the original author or authors.
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

package io.spring.start.site.extension.dependency.postgresql;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on PgVector.
 *
 * @author Eddú Meléndez
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("spring-ai-vectordb-pgvector")
class PgVectorProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CLASS_NAME = "org.testcontainers.containers.PostgreSQLContainer";

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer postgresqlServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver
			.doWith("pgvector", (service) -> serviceConnections.addServiceConnection(
					ServiceConnections.ServiceConnection.ofContainer("pgvector", service, TESTCONTAINERS_CLASS_NAME)));
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer pgvectorComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("pgvector",
				(service) -> composeFile.services()
					.add("pgvector",
							service.andThen((builder) -> builder.environment("POSTGRES_USER", "myuser")
								.environment("POSTGRES_DB", "mydatabase")
								.environment("POSTGRES_PASSWORD", "secret"))));
	}

}
