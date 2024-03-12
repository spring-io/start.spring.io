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

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Chroma.
 *
 * @author Eddú Meléndez
 */
@Configuration(proxyBeanMethods = false)
class SpringAiProjectGenerationConfiguration {

	private static final String CHROMA_TESTCONTAINERS_CLASS_NAME = "org.testcontainers.chromadb.ChromaDBContainer";

	private static final String MILVUS_TESTCONTAINERS_CLASS_NAME = "org.testcontainers.milvus.MilvusContainer";

	private static final String QDRANT_TESTCONTAINERS_CLASS_NAME = "org.testcontainers.qdrant.QdrantContainer";

	private static final String WEAVIATE_TESTCONTAINERS_CLASS_NAME = "org.testcontainers.weaviate.WeaviateContainer";

	@Bean
	@ConditionalOnRequestedDependency("spring-ai-vectordb-chroma")
	ServiceConnectionsCustomizer chromaServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver.doWith("chroma", (service) -> serviceConnections
			.addServiceConnection(ServiceConnection.ofContainer("chroma", service, CHROMA_TESTCONTAINERS_CLASS_NAME)));
	}

	@Bean
	@ConditionalOnRequestedDependency("spring-ai-vectordb-milvus")
	ServiceConnectionsCustomizer milvusServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver.doWith("milvus", (service) -> serviceConnections
			.addServiceConnection(ServiceConnection.ofContainer("milvus", service, MILVUS_TESTCONTAINERS_CLASS_NAME)));
	}

	@Bean
	@ConditionalOnRequestedDependency("spring-ai-vectordb-qdrant")
	ServiceConnectionsCustomizer qdrantServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver.doWith("qdrant", (service) -> serviceConnections
			.addServiceConnection(ServiceConnection.ofContainer("qdrant", service, QDRANT_TESTCONTAINERS_CLASS_NAME)));
	}

	@Bean
	@ConditionalOnRequestedDependency("spring-ai-vectordb-weaviate")
	ServiceConnectionsCustomizer weaviateServiceConnectionsCustomizer(DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> serviceResolver.doWith("weaviate",
				(service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("weaviate", service, WEAVIATE_TESTCONTAINERS_CLASS_NAME)));
	}

}
