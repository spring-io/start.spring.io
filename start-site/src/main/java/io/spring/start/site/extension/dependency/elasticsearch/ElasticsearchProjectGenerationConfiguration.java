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

package io.spring.start.site.extension.dependency.elasticsearch;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Elasticsearch.
 *
 * @author Moritz Halbritter
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
@Configuration(proxyBeanMethods = false)
class ElasticsearchProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CLASS_NAME = "org.testcontainers.elasticsearch.ElasticsearchContainer";

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer elasticsearchServiceConnectionsCustomizer(Build build,
			DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> {
			if (isElasticsearchEnabled(build)) {
				serviceResolver.doWith("elasticsearch", (service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("elasticsearch", service, TESTCONTAINERS_CLASS_NAME, false)));
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer elasticsearchComposeFileCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (composeFile) -> {
			if (isElasticsearchEnabled(build)) {
				serviceResolver.doWith("elasticsearch",
						(service) -> composeFile.services()
							.add("elasticsearch",
									service.andThen((builder) -> builder.environment("ELASTIC_PASSWORD", "secret")
										.environment("xpack.security.enabled", "false")
										.environment("discovery.type", "single-node"))));
			}
		};
	}

	private boolean isElasticsearchEnabled(Build build) {
		return build.dependencies().has("data-elasticsearch")
				|| build.dependencies().has("spring-ai-vectordb-elasticsearch");
	}

}
