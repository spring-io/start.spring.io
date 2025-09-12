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

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
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
	void springKafkaTestIsAdded() {
		ProjectRequest request = createProjectRequest("kafka");
		Dependency kafkaTest = Dependency.withId("spring-kafka-test", "org.springframework.kafka", "spring-kafka-test",
				null, Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependency(kafkaTest)
			.hasDependenciesSize(4);
	}

	@Test
	void springKafkaTestIsNotAddedWithoutKafka() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("web"))
			.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
			.hasDependenciesSize(2);
	}

	@Test
	void customizesBuildpacksBuilderWhenUsingMavenAndKafkaStreamsAndBoot34() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_4, "kafka-streams");
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
				<plugin>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-maven-plugin</artifactId>
					<configuration>
						<image>
							<builder>paketobuildpacks/builder-jammy-base:latest</builder>
						</image>
					</configuration>
				</plugin>
				""");
	}

	@Test
	void customizesBuildpacksBuilderWhenUsingMavenAndKafkaStreams() {
		ProjectRequest request = createProjectRequest("kafka-streams");
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
				<plugin>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-maven-plugin</artifactId>
					<configuration>
						<image>
							<runImage>paketobuildpacks/ubuntu-noble-run-base:latest</runImage>
						</image>
					</configuration>
				</plugin>
				""");
	}

	@Test
	void customizesBuildpacksBuilderWhenUsingGradleGroovyAndKafkaStreamsAndBoot34() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_4, "kafka-streams");
		assertThat(gradleBuild(request)).containsIgnoringWhitespaces("""
				tasks.named('bootBuildImage') {
					builder = 'paketobuildpacks/builder-jammy-base:latest'
				}
				""");
	}

	@Test
	void customizesBuildpacksBuilderWhenUsingGradleGroovyAndKafkaStreams() {
		ProjectRequest request = createProjectRequest("kafka-streams");
		assertThat(gradleBuild(request)).containsIgnoringWhitespaces("""
				tasks.named('bootBuildImage') {
					runImage = 'paketobuildpacks/ubuntu-noble-run-base:latest'
				}
				""");
	}

	@Test
	void customizesBuildpacksBuilderWhenUsingGradleKotlinAndKafkaStreamsAndBoot34() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_4, "kafka-streams");
		assertThat(gradleKotlinDslBuild(request)).containsIgnoringWhitespaces("""
				tasks.bootBuildImage {
				    builder = "paketobuildpacks/builder-jammy-base:latest"
				}
				""");
	}

	@Test
	void customizesBuildpacksBuilderWhenUsingGradleKotlinAndKafkaStreams() {
		ProjectRequest request = createProjectRequest("kafka-streams");
		assertThat(gradleKotlinDslBuild(request)).containsIgnoringWhitespaces("""
				tasks.bootBuildImage {
				    runImage = "paketobuildpacks/ubuntu-noble-run-base:latest"
				}
				""");
	}

}
