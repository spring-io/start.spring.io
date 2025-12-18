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

package io.spring.start.site.extension.dependency.testcontainers;

import java.util.stream.Stream;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TestcontainersProjectGenerationConfiguration}.
 *
 * @author Maciej Walkowiak
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 * @author Chris Bono
 * @author Moritz Halbritter
 * @author Ngoc Nhan
 */
class TestcontainersProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V3_5;

	@Test
	void buildWithOnlyTestContainers() {
		assertThat(generateProject("testcontainers")).mavenBuild()
			.hasDependency(getDependency(BOOT_VERSION, "testcontainers"));
	}

	@ParameterizedTest
	@MethodSource("supportedTestcontainersActiveMQEntriesBuild")
	void buildWithSpringBootAndTestcontainersActiveMQModule(String springBootDependencyId,
			String testcontainersArtifactId) {
		assertThat(generateProject("testcontainers", springBootDependencyId)).mavenBuild()
			.doesNotHaveBom("org.testcontainers", "testcontainers-bom")
			.hasDependency(getDependency(springBootDependencyId)
				.resolve(Version.parse(SupportedBootVersion.latest().getVersion())))
			.hasDependency("org.testcontainers", testcontainersArtifactId, null, "test")
			.hasDependency(getDependency(BOOT_VERSION, "testcontainers"));
	}

	@ParameterizedTest
	@MethodSource("supportedTestcontainersSpringAiEntriesBuild")
	void buildWithSpringBootAndTestcontainersSpringAiModule(String springBootDependencyId,
			String testcontainersArtifactId) {
		assertThat(generateProject("testcontainers", springBootDependencyId)).mavenBuild()
			.doesNotHaveBom("org.testcontainers", "testcontainers-bom")
			.hasDependency(getDependency(springBootDependencyId)
				.resolve(Version.parse(SupportedBootVersion.latest().getVersion())))
			.hasDependency("org.testcontainers", testcontainersArtifactId, null, "test")
			.hasDependency(getDependency(BOOT_VERSION, "testcontainers"));
	}

	static Stream<Arguments> supportedTestcontainersActiveMQEntriesBuild() {
		return Stream.of(Arguments.arguments("activemq", "activemq"), Arguments.arguments("artemis", "activemq"));
	}

	static Stream<Arguments> supportedTestcontainersSpringAiEntriesBuild() {
		return Stream.of(Arguments.arguments("spring-ai-vectordb-chroma", "chromadb"),
				Arguments.arguments("spring-ai-vectordb-milvus", "milvus"),
				Arguments.arguments("spring-ai-vectordb-mongodb-atlas", "mongodb"),
				Arguments.arguments("spring-ai-vectordb-qdrant", "qdrant"),
				Arguments.arguments("spring-ai-vectordb-weaviate", "weaviate"));
	}

	@ParameterizedTest
	@MethodSource("supportedEntriesHelpDocument")
	void linkToSupportedEntriesWhenTestContainerIsPresentIsAdded(String dependencyId, String docHref) {
		assertHelpDocument("testcontainers", dependencyId).contains(docHref);
	}

	@ParameterizedTest
	@MethodSource("supportedEntriesHelpDocument")
	void linkToSupportedEntriesWhenTestContainerIsNotPresentIsNotAdded(String dependencyId, String docHref) {
		assertHelpDocument(dependencyId).doesNotContain(docHref);
	}

	static Stream<Arguments> supportedEntriesHelpDocument() {
		return Stream.of(Arguments.arguments("amqp", "https://java.testcontainers.org/modules/rabbitmq/"),
				Arguments.arguments("amqp-streams", "https://java.testcontainers.org/modules/rabbitmq/"),
				Arguments.arguments("cloud-gcp", "https://java.testcontainers.org/modules/gcloud/"),
				Arguments.arguments("cloud-gcp-pubsub", "https://java.testcontainers.org/modules/gcloud/"),
				Arguments.arguments("cloud-starter-consul-config", "https://java.testcontainers.org/modules/consul/"),
				Arguments.arguments("cloud-starter-vault-config", "https://java.testcontainers.org/modules/vault/"),
				Arguments.arguments("data-cassandra", "https://java.testcontainers.org/modules/databases/cassandra/"),
				Arguments.arguments("data-cassandra-reactive",
						"https://java.testcontainers.org/modules/databases/cassandra/"),
				Arguments.arguments("data-couchbase", "https://java.testcontainers.org/modules/databases/couchbase/"),
				Arguments.arguments("data-couchbase-reactive",
						"https://java.testcontainers.org/modules/databases/couchbase/"),
				Arguments.arguments("data-elasticsearch", "https://java.testcontainers.org/modules/elasticsearch/"),
				Arguments.arguments("data-mongodb", "https://java.testcontainers.org/modules/databases/mongodb/"),
				Arguments.arguments("data-mongodb-reactive",
						"https://java.testcontainers.org/modules/databases/mongodb/"),
				Arguments.arguments("data-neo4j", "https://java.testcontainers.org/modules/databases/neo4j/"),
				Arguments.arguments("data-r2dbc", "https://java.testcontainers.org/modules/databases/r2dbc/"),
				Arguments.arguments("db2", "https://java.testcontainers.org/modules/databases/db2"),
				Arguments.arguments("kafka", "https://java.testcontainers.org/modules/kafka/"),
				Arguments.arguments("kafka-streams", "https://java.testcontainers.org/modules/kafka/"),
				Arguments.arguments("mariadb", "https://java.testcontainers.org/modules/databases/mariadb/"),
				Arguments.arguments("mysql", "https://java.testcontainers.org/modules/databases/mysql/"),
				Arguments.arguments("oracle", "https://hub.docker.com/r/gvenzl/oracle-free"),
				Arguments.arguments("postgresql", "https://java.testcontainers.org/modules/databases/postgres/"),
				Arguments.arguments("pulsar", "https://java.testcontainers.org/modules/pulsar/"),
				Arguments.arguments("pulsar-reactive", "https://java.testcontainers.org/modules/pulsar/"),
				Arguments.arguments("solace", "https://java.testcontainers.org/modules/solace/"),
				Arguments.arguments("sqlserver", "https://java.testcontainers.org/modules/databases/mssqlserver/"));
	}

	@Test
	void linkToSupportedEntriesWhenTwoMatchesArePresentOnlyAddLinkOnce() {
		assertHelpDocument("testcontainers", "data-mongodb", "data-mongodb-reactive")
			.containsOnlyOnce("https://java.testcontainers.org/modules/databases/mongodb/");
	}

	@Test
	void buildWithSpringBoot31DoesNotIncludeBom() {
		assertThat(generateProject("testcontainers")).mavenBuild()
			.doesNotHaveBom("org.testcontainers", "testcontainers-bom")
			.hasDependency(getDependency(BOOT_VERSION, "testcontainers"));
	}

	@Test
	void buildWithSpringBoot31IncludeSpringBootTestcontainers() {
		assertThat(generateProject("testcontainers")).mavenBuild()
			.hasDependency("org.springframework.boot", "spring-boot-testcontainers", null, "test");
	}

	@Test
	void buildWithSpringBoot31IncludeTestcontainersSection() {
		assertHelpDocument("testcontainers").contains("Spring Boot Testcontainers support");
	}

	@Test
	void testApplicationWithGroovyAndGenericContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-redis");
		request.setLanguage("groovy");
		assertThat(generateProject(request))
			.textFile("src/test/groovy/com/example/demo/TestcontainersConfiguration.groovy")
			.isEqualToNormalizingNewlines("""
					package com.example.demo

					import org.springframework.boot.test.context.TestConfiguration
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection
					import org.springframework.context.annotation.Bean
					import org.testcontainers.containers.GenericContainer
					import org.testcontainers.utility.DockerImageName

					@TestConfiguration(proxyBeanMethods = false)
					class TestcontainersConfiguration {

						@Bean
						@ServiceConnection(name = "redis")
						GenericContainer redisContainer() {
							new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379)
						}

					}
					""");
	}

	@Test
	void testApplicationWithJavaAndGenericContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-redis");
		request.setLanguage("java");
		assertThat(generateProject(request)).textFile("src/test/java/com/example/demo/TestcontainersConfiguration.java")
			.isEqualToNormalizingNewlines("""
					package com.example.demo;

					import org.springframework.boot.test.context.TestConfiguration;
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
					import org.springframework.context.annotation.Bean;
					import org.testcontainers.containers.GenericContainer;
					import org.testcontainers.utility.DockerImageName;

					@TestConfiguration(proxyBeanMethods = false)
					class TestcontainersConfiguration {

						@Bean
						@ServiceConnection(name = "redis")
						GenericContainer<?> redisContainer() {
							return new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
						}

					}
					""");
	}

	@Test
	void testApplicationWithKotlinAndGenericContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-redis");
		request.setLanguage("kotlin");
		assertThat(generateProject(request)).textFile("src/test/kotlin/com/example/demo/TestcontainersConfiguration.kt")
			.isEqualToNormalizingNewlines("""
					package com.example.demo

					import org.springframework.boot.test.context.TestConfiguration
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection
					import org.springframework.context.annotation.Bean
					import org.testcontainers.containers.GenericContainer
					import org.testcontainers.utility.DockerImageName

					@TestConfiguration(proxyBeanMethods = false)
					class TestcontainersConfiguration {

						@Bean
						@ServiceConnection(name = "redis")
						fun redisContainer(): GenericContainer<*> {
							return GenericContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379)
						}

					}
					""");
	}

	@Test
	void testApplicationWithGroovyAndSpecificContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-cassandra");
		request.setLanguage("groovy");
		ProjectStructure projectStructure = generateProject(request);
		assertThat(projectStructure).textFile("src/test/groovy/com/example/demo/TestDemoApplication.groovy")
			.isEqualToNormalizingNewlines("""
					package com.example.demo

					import org.springframework.boot.SpringApplication

					class TestDemoApplication {

						static void main(String[] args) {
							SpringApplication.from(DemoApplication::main).with(TestcontainersConfiguration).run(args)
						}

					}
					""");
		assertThat(projectStructure).textFile("src/test/groovy/com/example/demo/TestcontainersConfiguration.groovy")
			.isEqualToNormalizingNewlines("""
					package com.example.demo

					import org.springframework.boot.test.context.TestConfiguration
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection
					import org.springframework.context.annotation.Bean
					import org.testcontainers.cassandra.CassandraContainer
					import org.testcontainers.utility.DockerImageName

					@TestConfiguration(proxyBeanMethods = false)
					class TestcontainersConfiguration {

						@Bean
						@ServiceConnection
						CassandraContainer cassandraContainer() {
							new CassandraContainer(DockerImageName.parse("cassandra:latest"))
						}

					}
					""");
		assertThat(projectStructure).textFile("src/test/groovy/com/example/demo/DemoApplicationTests.groovy")
			.isEqualToNormalizingNewlines("""
					package com.example.demo

					import org.junit.jupiter.api.Test
					import org.springframework.boot.test.context.SpringBootTest
					import org.springframework.context.annotation.Import

					@Import(TestcontainersConfiguration)
					@SpringBootTest
					class DemoApplicationTests {

						@Test
						void contextLoads() {
						}

					}
					""");
	}

	@Test
	void testApplicationWithJavaAndSpecificContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-cassandra");
		request.setLanguage("java");
		ProjectStructure projectStructure = generateProject(request);
		assertThat(projectStructure).textFile("src/test/java/com/example/demo/TestDemoApplication.java")
			.isEqualToNormalizingNewlines(
					"""
							package com.example.demo;

							import org.springframework.boot.SpringApplication;

							public class TestDemoApplication {

								public static void main(String[] args) {
									SpringApplication.from(DemoApplication::main).with(TestcontainersConfiguration.class).run(args);
								}

							}
							""");
		assertThat(projectStructure).textFile("src/test/java/com/example/demo/TestcontainersConfiguration.java")
			.isEqualToNormalizingNewlines("""
					package com.example.demo;

					import org.springframework.boot.test.context.TestConfiguration;
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
					import org.springframework.context.annotation.Bean;
					import org.testcontainers.cassandra.CassandraContainer;
					import org.testcontainers.utility.DockerImageName;

					@TestConfiguration(proxyBeanMethods = false)
					class TestcontainersConfiguration {

						@Bean
						@ServiceConnection
						CassandraContainer cassandraContainer() {
							return new CassandraContainer(DockerImageName.parse("cassandra:latest"));
						}

					}
					""");
		assertThat(projectStructure).textFile("src/test/java/com/example/demo/DemoApplicationTests.java")
			.isEqualToNormalizingNewlines("""
					package com.example.demo;

					import org.junit.jupiter.api.Test;
					import org.springframework.boot.test.context.SpringBootTest;
					import org.springframework.context.annotation.Import;

					@Import(TestcontainersConfiguration.class)
					@SpringBootTest
					class DemoApplicationTests {

						@Test
						void contextLoads() {
						}

					}
					""");
	}

	@Test
	void testApplicationWithKotlinAndSpecificContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-cassandra");
		request.setLanguage("kotlin");
		ProjectStructure projectStructure = generateProject(request);
		assertThat(projectStructure).textFile("src/test/kotlin/com/example/demo/TestDemoApplication.kt")
			.isEqualToNormalizingNewlines("""
					package com.example.demo

					import org.springframework.boot.fromApplication
					import org.springframework.boot.with


					fun main(args: Array<String>) {
						fromApplication<DemoApplication>().with(TestcontainersConfiguration::class).run(*args)
					}
					""");
		assertThat(projectStructure).textFile("src/test/kotlin/com/example/demo/TestcontainersConfiguration.kt")
			.isEqualToNormalizingNewlines("""
					package com.example.demo

					import org.springframework.boot.test.context.TestConfiguration
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection
					import org.springframework.context.annotation.Bean
					import org.testcontainers.cassandra.CassandraContainer
					import org.testcontainers.utility.DockerImageName

					@TestConfiguration(proxyBeanMethods = false)
					class TestcontainersConfiguration {

						@Bean
						@ServiceConnection
						fun cassandraContainer(): CassandraContainer {
							return CassandraContainer(DockerImageName.parse("cassandra:latest"))
						}

					}
					""");
		assertThat(projectStructure).textFile("src/test/kotlin/com/example/demo/DemoApplicationTests.kt")
			.isEqualToNormalizingNewlines("""
					package com.example.demo

					import org.junit.jupiter.api.Test
					import org.springframework.boot.test.context.SpringBootTest
					import org.springframework.context.annotation.Import

					@Import(TestcontainersConfiguration::class)
					@SpringBootTest
					class DemoApplicationTests {

						@Test
						fun contextLoads() {
						}

					}
					""");
	}

	@Test
	void shouldAddHelpSection() {
		assertHelpDocument("testcontainers", "data-mongodb", "postgresql").contains(
				"https://docs.spring.io/spring-boot/3.5.0/reference/testing/testcontainers.html#testing.testcontainers")
			.contains(
					"https://docs.spring.io/spring-boot/3.5.0/reference/features/dev-services.html#features.dev-services.testcontainers")
			.contains("mongo:latest")
			.contains("postgres:latest");
	}

	private ProjectStructure generateProject(String... dependencies) {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, dependencies);
		request.setType("maven-build");
		return generateProject(request);
	}

	private TextAssert assertHelpDocument(String... dependencyIds) {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, dependencyIds);
		return assertThat(helpDocument(request));
	}

}
