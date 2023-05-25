/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.start.site.extension.dependency.mongodb;

import java.util.List;
import java.util.Map;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.spring.container.dockercompose.DockerComposeFileCustomizer;
import io.spring.initializr.generator.spring.container.dockercompose.DockerComposeService;
import io.spring.start.site.container.DockerImages;
import io.spring.start.site.container.DockerImages.DockerImage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on MongoDB.
 *
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("docker-compose")
class MongoDbDockerComposeProjectGenerationConfiguration {

	@Bean
	DockerComposeFileCustomizer mongoDbDockerComposeFileCustomizer(Build build) {
		return (file) -> {
			if (build.dependencies().has("data-mongodb") || build.dependencies().has("data-mongodb-reactive")) {
				DockerImage image = DockerImages.mongoDb();
				DockerComposeService service = new DockerComposeService(
						"mongodb", image.image(), image.tag(), image.website(), Map.of("MONGO_INITDB_ROOT_USERNAME",
								"root", "MONGO_INITDB_ROOT_PASSWORD", "secret", "MONGO_INITDB_DATABASE", "mydatabase"),
						List.of(27017));
				file.addService(service);
			}
		};
	}

}
