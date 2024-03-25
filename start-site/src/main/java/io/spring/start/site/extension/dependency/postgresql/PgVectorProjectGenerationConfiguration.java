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

import java.util.Map;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
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

	private static final VersionRange TESTCONTAINERS_1_19_7_OR_LATER = VersionParser.DEFAULT.parseRange("1.19.7");

	private final MavenVersionResolver versionResolver;

	private final ProjectDescription description;

	PgVectorProjectGenerationConfiguration(MavenVersionResolver versionResolver, ProjectDescription description) {
		this.versionResolver = versionResolver;
		this.description = description;
	}

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer pgvectorServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		Map<String, String> resolve = this.versionResolver.resolveDependencies("org.springframework.boot",
				"spring-boot-dependencies", this.description.getPlatformVersion().toString());
		String testcontainersVersion = resolve.get("org.testcontainers:testcontainers");
		return (serviceConnections) -> {
			if (TESTCONTAINERS_1_19_7_OR_LATER.match(Version.parse(testcontainersVersion))) {
				serviceResolver.doWith("pgvector", (service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("pgvector", service, TESTCONTAINERS_CLASS_NAME)));
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer pgvectorComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("pgvector",
				(service) -> composeFile.services()
					.add("pgvector",
							service.andThen((builder) -> builder.environment("POSTGRES_USER", "myuser")
								.environment("POSTGRES_DB", "mydatabase")
								.environment("POSTGRES_PASSWORD", "secret")
								.label("org.springframework.boot.service-connection", "postgres"))));
	}

}
