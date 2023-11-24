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

package io.spring.start.site.extension.dependency.testcontainers;

import java.util.stream.Stream;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.web.project.ProjectRequest;
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
 */
class TestcontainersProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void buildWithOnlyTestContainers() {
		assertThat(generateProject("3.1.0", "testcontainers")).mavenBuild()
			.hasDependency(getDependency("testcontainers"));
	}

	@ParameterizedTest
	@MethodSource("supportedEntriesBuild")
	void buildWithSupportedEntries(String springBootDependencyId, String testcontainersArtifactId) {
		assertThat(generateProject("3.1.0", "testcontainers", springBootDependencyId)).mavenBuild()
			.hasDependency(getDependency(springBootDependencyId).resolve(Version.parse("3.1.0")))
			.hasDependency("org.testcontainers", testcontainersArtifactId, null, "test")
			.hasDependency(getDependency("testcontainers"));
	}

	@Test
	void buildWithSpringBoot32AndOracleJdbcDriverUsesOracleFree() {
		assertThat(generateProject("3.2.0", "testcontainers", "oracle")).mavenBuild()
			.doesNotHaveBom("org.testcontainers", "testcontainers-bom")
			.hasDependency(getDependency("oracle").resolve(Version.parse("3.2.0")))
			.hasDependency("org.testcontainers", "oracle-free", null, "test")
			.hasDependency(getDependency("testcontainers"));
	}

	static Stream<Arguments> supportedEntriesBuild() {
		return Stream.of(Arguments.arguments("amqp", "rabbitmq"), Arguments.arguments("cloud-gcp", "gcloud"),
				Arguments.arguments("cloud-gcp-pubsub", "gcloud"), Arguments.arguments("data-cassandra", "cassandra"),
				Arguments.arguments("data-cassandra-reactive", "cassandra"),
				Arguments.arguments("data-couchbase", "couchbase"),
				Arguments.arguments("data-couchbase-reactive", "couchbase"),
				Arguments.arguments("data-elasticsearch", "elasticsearch"),
				Arguments.arguments("data-mongodb", "mongodb"), Arguments.arguments("data-mongodb-reactive", "mongodb"),
				Arguments.arguments("data-neo4j", "neo4j"), Arguments.arguments("data-r2dbc", "r2dbc"),
				Arguments.arguments("db2", "db2"), Arguments.arguments("kafka", "kafka"),
				Arguments.arguments("kafka-streams", "kafka"), Arguments.arguments("mariadb", "mariadb"),
				Arguments.arguments("mysql", "mysql"), Arguments.arguments("postgresql", "postgresql"),
				Arguments.arguments("oracle", "oracle-xe"), Arguments.arguments("pulsar", "pulsar"),
				Arguments.arguments("pulsar-reactive", "pulsar"), Arguments.arguments("solace", "solace"),
				Arguments.arguments("sqlserver", "mssqlserver"));
	}

	@ParameterizedTest
	@MethodSource("supportedEntriesHelpDocument")
	void linkToSupportedEntriesWhenTestContainerIsPresentIsAdded(String dependencyId, String docHref) {
		assertHelpDocument("3.1.0", "testcontainers", dependencyId)
			.contains("https://java.testcontainers.org/modules/" + docHref);
	}

	@ParameterizedTest
	@MethodSource("supportedEntriesHelpDocument")
	void linkToSupportedEntriesWhenTestContainerIsNotPresentIsNotAdded(String dependencyId, String docHref) {
		assertHelpDocument("3.1.0", dependencyId).doesNotContain("https://java.testcontainers.org/modules/" + docHref);
	}

	static Stream<Arguments> supportedEntriesHelpDocument() {
		return Stream.of(Arguments.arguments("amqp", "rabbitmq/"), Arguments.arguments("cloud-gcp", "gcloud/"),
				Arguments.arguments("cloud-gcp-pubsub", "gcloud/"),
				Arguments.arguments("cloud-starter-consul-config", "consul/"),
				Arguments.arguments("cloud-starter-vault-config", "vault/"),
				Arguments.arguments("data-cassandra", "databases/cassandra/"),
				Arguments.arguments("data-cassandra-reactive", "databases/cassandra/"),
				Arguments.arguments("data-couchbase", "databases/couchbase/"),
				Arguments.arguments("data-couchbase-reactive", "databases/couchbase/"),
				Arguments.arguments("data-elasticsearch", "elasticsearch/"),
				Arguments.arguments("data-mongodb", "databases/mongodb/"),
				Arguments.arguments("data-mongodb-reactive", "databases/mongodb/"),
				Arguments.arguments("data-neo4j", "databases/neo4j/"),
				Arguments.arguments("data-r2dbc", "databases/r2dbc/"), Arguments.arguments("db2", "databases/db2"),
				Arguments.arguments("kafka", "kafka/"), Arguments.arguments("kafka-streams", "kafka/"),
				Arguments.arguments("mariadb", "databases/mariadb/"), Arguments.arguments("mysql", "databases/mysql/"),
				Arguments.arguments("oracle", "databases/oraclexe/"),
				Arguments.arguments("postgresql", "databases/postgres/"), Arguments.arguments("pulsar", "pulsar/"),
				Arguments.arguments("pulsar-reactive", "pulsar/"), Arguments.arguments("solace", "solace/"),
				Arguments.arguments("sqlserver", "databases/mssqlserver/"));
	}

	@Test
	void linkToSupportedEntriesWhenTwoMatchesArePresentOnlyAddLinkOnce() {
		assertHelpDocument("3.1.0", "testcontainers", "data-mongodb", "data-mongodb-reactive")
			.containsOnlyOnce("https://java.testcontainers.org/modules/databases/mongodb/");
	}

	@Test
	void buildWithSpringBoot31DoesNotIncludeBom() {
		assertThat(generateProject("3.1.0", "testcontainers")).mavenBuild()
			.doesNotHaveBom("org.testcontainers", "testcontainers-bom")
			.hasDependency(getDependency("testcontainers"));
	}

	@Test
	void buildWithSpringBoot31IncludeSpringBootTestcontainers() {
		assertThat(generateProject("3.1.0", "testcontainers")).mavenBuild()
			.hasDependency("org.springframework.boot", "spring-boot-testcontainers", null, "test");
	}

	@Test
	void buildWithSpringBoot31IncludeTestcontainersSection() {
		assertHelpDocument("3.1.0", "testcontainers").contains("Spring Boot Testcontainers support");
	}

	@Test
	void testApplicationWithGroovyAndGenericContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-redis");
		request.setBootVersion("3.1.0");
		request.setLanguage("groovy");
		assertThat(generateProject(request)).textFile("src/test/groovy/com/example/demo/TestDemoApplication.groovy")
			.isEqualTo("""
					package com.example.demo

					import org.springframework.boot.SpringApplication
					import org.springframework.boot.test.context.TestConfiguration
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection
					import org.springframework.context.annotation.Bean
					import org.testcontainers.containers.GenericContainer
					import org.testcontainers.utility.DockerImageName

					@TestConfiguration(proxyBeanMethods = false)
					class TestDemoApplication {

						@Bean
						@ServiceConnection(name = "redis")
						GenericContainer redisContainer() {
							new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379)
						}

						static void main(String[] args) {
							SpringApplication.from(DemoApplication::main).with(TestDemoApplication).run(args)
						}

					}
					""");
	}

	@Test
	void testApplicationWithJavaAndGenericContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-redis");
		request.setBootVersion("3.1.0");
		request.setLanguage("java");
		assertThat(generateProject(request)).textFile("src/test/java/com/example/demo/TestDemoApplication.java")
			.isEqualTo("""
					package com.example.demo;

					import org.springframework.boot.SpringApplication;
					import org.springframework.boot.test.context.TestConfiguration;
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
					import org.springframework.context.annotation.Bean;
					import org.testcontainers.containers.GenericContainer;
					import org.testcontainers.utility.DockerImageName;

					@TestConfiguration(proxyBeanMethods = false)
					public class TestDemoApplication {

						@Bean
						@ServiceConnection(name = "redis")
						GenericContainer<?> redisContainer() {
							return new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
						}

						public static void main(String[] args) {
							SpringApplication.from(DemoApplication::main).with(TestDemoApplication.class).run(args);
						}

					}
					""");
	}

	@Test
	void testApplicationWithKotlinAndGenericContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-redis");
		request.setBootVersion("3.1.1");
		request.setLanguage("kotlin");
		assertThat(generateProject(request)).textFile("src/test/kotlin/com/example/demo/TestDemoApplication.kt")
			.isEqualTo("""
					package com.example.demo

					import org.springframework.boot.fromApplication
					import org.springframework.boot.test.context.TestConfiguration
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection
					import org.springframework.boot.with
					import org.springframework.context.annotation.Bean
					import org.testcontainers.containers.GenericContainer
					import org.testcontainers.utility.DockerImageName

					@TestConfiguration(proxyBeanMethods = false)
					class TestDemoApplication {

						@Bean
						@ServiceConnection(name = "redis")
						fun redisContainer(): GenericContainer<*> {
							return GenericContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379)
						}

					}

					fun main(args: Array<String>) {
						fromApplication<DemoApplication>().with(TestDemoApplication::class).run(*args)
					}
					""");
	}

	@Test
	void testApplicationWithGroovyAndSpecificContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-cassandra");
		request.setBootVersion("3.1.0");
		request.setLanguage("groovy");
		assertThat(generateProject(request)).textFile("src/test/groovy/com/example/demo/TestDemoApplication.groovy")
			.isEqualTo("""
					package com.example.demo

					import org.springframework.boot.SpringApplication
					import org.springframework.boot.test.context.TestConfiguration
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection
					import org.springframework.context.annotation.Bean
					import org.testcontainers.containers.CassandraContainer
					import org.testcontainers.utility.DockerImageName

					@TestConfiguration(proxyBeanMethods = false)
					class TestDemoApplication {

						@Bean
						@ServiceConnection
						CassandraContainer cassandraContainer() {
							new CassandraContainer<>(DockerImageName.parse("cassandra:latest"))
						}

						static void main(String[] args) {
							SpringApplication.from(DemoApplication::main).with(TestDemoApplication).run(args)
						}

					}
					""");
	}

	@Test
	void testApplicationWithJavaAndSpecificContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-cassandra");
		request.setBootVersion("3.1.0");
		request.setLanguage("java");
		assertThat(generateProject(request)).textFile("src/test/java/com/example/demo/TestDemoApplication.java")
			.isEqualTo("""
					package com.example.demo;

					import org.springframework.boot.SpringApplication;
					import org.springframework.boot.test.context.TestConfiguration;
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
					import org.springframework.context.annotation.Bean;
					import org.testcontainers.containers.CassandraContainer;
					import org.testcontainers.utility.DockerImageName;

					@TestConfiguration(proxyBeanMethods = false)
					public class TestDemoApplication {

						@Bean
						@ServiceConnection
						CassandraContainer<?> cassandraContainer() {
							return new CassandraContainer<>(DockerImageName.parse("cassandra:latest"));
						}

						public static void main(String[] args) {
							SpringApplication.from(DemoApplication::main).with(TestDemoApplication.class).run(args);
						}

					}
					""");
	}

	@Test
	void testApplicationWithKotlinAndSpecificContainerIsContributed() {
		ProjectRequest request = createProjectRequest("testcontainers", "data-cassandra");
		request.setBootVersion("3.1.1");
		request.setLanguage("kotlin");
		assertThat(generateProject(request)).textFile("src/test/kotlin/com/example/demo/TestDemoApplication.kt")
			.isEqualTo("""
					package com.example.demo

					import org.springframework.boot.fromApplication
					import org.springframework.boot.test.context.TestConfiguration
					import org.springframework.boot.testcontainers.service.connection.ServiceConnection
					import org.springframework.boot.with
					import org.springframework.context.annotation.Bean
					import org.testcontainers.containers.CassandraContainer
					import org.testcontainers.utility.DockerImageName

					@TestConfiguration(proxyBeanMethods = false)
					class TestDemoApplication {

						@Bean
						@ServiceConnection
						fun cassandraContainer(): CassandraContainer<*> {
							return CassandraContainer(DockerImageName.parse("cassandra:latest"))
						}

					}

					fun main(args: Array<String>) {
						fromApplication<DemoApplication>().with(TestDemoApplication::class).run(*args)
					}
					""");
	}

	@Test
	void shouldAddHelpSection() {
		assertHelpDocument("3.1.5", "testcontainers", "data-mongodb", "postgresql").contains(
				"https://docs.spring.io/spring-boot/docs/3.1.5/reference/html/features.html#features.testing.testcontainers")
			.contains(
					"https://docs.spring.io/spring-boot/docs/3.1.5/reference/html/features.html#features.testing.testcontainers.at-development-time")
			.contains("mongo:latest")
			.contains("postgres:latest");
	}

	private ProjectStructure generateProject(String platformVersion, String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setBootVersion(platformVersion);
		request.setType("maven-build");
		return generateProject(request);
	}

	private TextAssert assertHelpDocument(String platformVersion, String... dependencyIds) {
		ProjectRequest request = createProjectRequest(dependencyIds);
		request.setBootVersion(platformVersion);
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md"));
	}

}
