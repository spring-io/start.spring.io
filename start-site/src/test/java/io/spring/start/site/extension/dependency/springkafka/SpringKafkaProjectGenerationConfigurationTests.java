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

package io.spring.start.site.extension.dependency.springkafka;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringKafkaProjectGenerationConfiguration}.
 *
 * @author Wonwoo Lee
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class SpringKafkaProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void customizesBuildpacksBuilderWhenUsingMavenAndKafkaStreams() {
		ProjectRequest request = createProjectRequest("kafka-streams");
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
				<plugin>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-maven-plugin</artifactId>
					<configuration>
						<image>
							<runImage>paketobuildpacks/ubuntu-noble-run:latest</runImage>
						</image>
					</configuration>
				</plugin>
				""");
	}

	@Test
	void customizesBuildpacksBuilderWhenUsingGradleGroovyAndKafkaStreams() {
		ProjectRequest request = createProjectRequest("kafka-streams");
		assertThat(gradleBuild(request)).containsIgnoringWhitespaces("""
				tasks.named('bootBuildImage') {
					runImage = 'paketobuildpacks/ubuntu-noble-run:latest'
				}
				""");
	}

	@Test
	void customizesBuildpacksBuilderWhenUsingGradleKotlinAndKafkaStreams() {
		ProjectRequest request = createProjectRequest("kafka-streams");
		assertThat(gradleKotlinDslBuild(request)).containsIgnoringWhitespaces("""
				tasks.bootBuildImage {
				    runImage = "paketobuildpacks/ubuntu-noble-run:latest"
				}
				""");
	}

}
