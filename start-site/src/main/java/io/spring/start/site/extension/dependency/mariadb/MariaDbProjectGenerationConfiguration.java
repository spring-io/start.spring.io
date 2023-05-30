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

package io.spring.start.site.extension.dependency.mariadb;

import java.util.List;
import java.util.Map;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.spring.container.dockercompose.DockerComposeFileCustomizer;
import io.spring.initializr.generator.spring.container.dockercompose.DockerComposeService;
import io.spring.start.site.container.DockerImages;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on MariaDB.
 *
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("mariadb")
class MariaDbProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	DockerComposeFileCustomizer mariaDbDockerComposeFileCustomizer() {
		return (composeFile) -> {
			DockerImages.DockerImage image = DockerImages.mariaDb();
			composeFile
				.addService(
						new DockerComposeService("mariadb", image.image(), image.tag(), image.website(),
								Map.of("MARIADB_ROOT_PASSWORD", "verysecret", "MARIADB_USER", "myuser",
										"MARIADB_PASSWORD", "secret", "MARIADB_DATABASE", "mydatabase"),
								List.of(3306)));
		};
	}

}
