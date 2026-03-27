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

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.container.docker.compose.ComposeConfig;
import io.spring.initializr.generator.container.docker.compose.ComposeServiceConfig;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import io.spring.start.site.container.Testcontainers;
import io.spring.start.site.container.Testcontainers.Container;
import io.spring.start.site.container.Testcontainers.SupportedContainer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Milvus.
 *
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("spring-ai-vectordb-milvus")
class SpringAiMilvusProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer milvusServiceConnectionsCustomizer(DockerServiceResolver serviceResolver,
			Testcontainers testcontainers) {
		Container container = testcontainers.getContainer(SupportedContainer.MILVUS);
		return (serviceConnections) -> serviceResolver.doWith("milvus",
				(service) -> serviceConnections.addServiceConnection(
						ServiceConnection.ofContainer("milvus", service, container.className(), container.generic())));
	}

	@Bean
	@ConditionalOnPlatformVersion("4.1.0-RC1")
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer mongodbAtlasComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("milvus", (service) -> {
			String embedEtcd = """
					listen-client-urls: http://0.0.0.0:2379
					advertise-client-urls: http://0.0.0.0:2379
					""";
			composeFile.configs().add("embedEtcd", ComposeConfig.Builder.forContent(embedEtcd).build());
			composeFile.services()
				.add("milvus",
						service.andThen((builder) -> builder.environment("COMMON_STORAGETYPE", "local")
							.environment("DEPLOY_MODE", "STANDALONE")
							.environment("ETCD_USE_EMBED", "true")
							.environment("ETCD_DATA_DIR", "/var/lib/milvus/etcd")
							.environment("ETCD_CONFIG_PATH", "/milvus/configs/embedEtcd.yaml")
							.command("milvus run standalone")
							.config(ComposeServiceConfig.ofLong("embedEtcd", "/milvus/configs/embedEtcd.yaml"))));
		});
	}

}
