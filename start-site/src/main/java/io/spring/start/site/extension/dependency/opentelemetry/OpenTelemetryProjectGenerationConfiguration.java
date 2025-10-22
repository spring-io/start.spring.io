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

package io.spring.start.site.extension.dependency.opentelemetry;

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
 * Configuration for generation of projects that depend on OpenTelemetry.
 *
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("opentelemetry")
public class OpenTelemetryProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer openTelemetryServiceConnectionsCustomizer(Build build,
			DockerServiceResolver serviceResolver, Testcontainers testcontainers) {
		return (serviceConnections) -> {
			Container container = testcontainers.getContainer(SupportedContainer.GRAFANA_LGTM);
			serviceResolver.doWith("grafanaLgtm", (service) -> serviceConnections.addServiceConnection(
					ServiceConnection.ofContainer("grafanaLgtm", service, container.className(), container.generic())));
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer openTelemetryComposeFileCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("grafanaLgtm",
				(service) -> composeFile.services().add("grafana-lgtm", service));
	}

}
