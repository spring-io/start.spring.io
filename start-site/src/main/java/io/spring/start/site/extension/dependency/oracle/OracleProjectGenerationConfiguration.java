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

package io.spring.start.site.extension.dependency.oracle;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Oracle.
 *
 * @author Moritz Halbritter
 * @author Stephane Nicoll
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("oracle")
class OracleProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer oracleServiceConnectionsCustomizer(DockerServiceResolver serviceResolver,
			ProjectDescription projectDescription) {
		OracleContainer oracleContainer = OracleContainer.forVersion(projectDescription.getPlatformVersion());
		return (serviceConnections) -> serviceResolver.doWith(oracleContainer.serviceId,
				(service) -> serviceConnections.addServiceConnection(ServiceConnection
					.ofContainer(oracleContainer.serviceId, service, oracleContainer.testcontainersClassName, false)));
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer oracleComposeFileCustomizer(DockerServiceResolver serviceResolver,
			ProjectDescription projectDescription) {
		OracleContainer oracleContainer = OracleContainer.forVersion(projectDescription.getPlatformVersion());
		return (composeFile) -> serviceResolver.doWith(oracleContainer.serviceId, (service) -> composeFile.services()
			.add("oracle", service.andThen((builder) -> builder.environment("ORACLE_PASSWORD", "secret"))));
	}

	private enum OracleContainer {

		FREE("oracleFree", "org.testcontainers.oracle.OracleContainer"),

		XE("oracleXe", "org.testcontainers.containers.OracleContainer");

		private static final VersionRange SPRING_BOOT_3_2_0_OR_LATER = VersionParser.DEFAULT.parseRange("3.2.0");

		private final String serviceId;

		private final String testcontainersClassName;

		OracleContainer(String serviceId, String testcontainersClassName) {
			this.serviceId = serviceId;
			this.testcontainersClassName = testcontainersClassName;
		}

		static OracleContainer forVersion(Version version) {
			return SPRING_BOOT_3_2_0_OR_LATER.match(version) ? FREE : XE;
		}

	}

}
