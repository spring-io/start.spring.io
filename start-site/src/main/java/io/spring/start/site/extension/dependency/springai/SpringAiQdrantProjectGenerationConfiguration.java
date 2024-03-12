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

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Qdrant.
 *
 * @author Eddú Meléndez
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("spring-ai-vectordb-qdrant")
class SpringAiQdrantProjectGenerationConfiguration {

	private static final String TESTCONTAINERS_CLASS_NAME = "org.testcontainers.qdrant.QdrantContainer";

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer qdrantServiceConnectionsCustomizer(Build build,
			DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> {
			if (SpringAiVersion.version1OrLater(build)) {
				serviceResolver.doWith("qdrant", (service) -> serviceConnections
					.addServiceConnection(ServiceConnection.ofContainer("qdrant", service, TESTCONTAINERS_CLASS_NAME)));
			}
		};
	}

}
