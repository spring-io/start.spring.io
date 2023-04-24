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
 */
class TestcontainersProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void buildWithOnlyTestContainers() {
		assertThat(generateProject("3.0.0", "testcontainers")).mavenBuild()
			.hasBom("org.testcontainers", "testcontainers-bom", "${testcontainers.version}")
			.hasDependency(getDependency("testcontainers"));
	}

	@ParameterizedTest
	@MethodSource("supportedEntriesBuild")
	void buildWithSupportedEntries(String springBootDependencyId, String testcontainersArtifactId) {
		assertThat(generateProject("3.0.0", "testcontainers", springBootDependencyId)).mavenBuild()
			.hasBom("org.testcontainers", "testcontainers-bom", "${testcontainers.version}")
			.hasDependency(getDependency(springBootDependencyId))
			.hasDependency("org.testcontainers", testcontainersArtifactId, null, "test")
			.hasDependency(getDependency("testcontainers"));
	}

	static Stream<Arguments> supportedEntriesBuild() {
		return Stream.of(Arguments.arguments("amqp", "rabbitmq"), Arguments.arguments("data-cassandra", "cassandra"),
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
				Arguments.arguments("pulsar-reactive", "pulsar"), Arguments.arguments("sqlserver", "mssqlserver"));
	}

	@ParameterizedTest
	@MethodSource("supportedEntriesHelpDocument")
	void linkToSupportedEntriesWhenTestContainerIsPresentIsAdded(String dependencyId, String docHref) {
		assertHelpDocument("3.0.0", "testcontainers", dependencyId)
			.contains("https://www.testcontainers.org/modules/" + docHref);
	}

	@ParameterizedTest
	@MethodSource("supportedEntriesHelpDocument")
	void linkToSupportedEntriesWhenTestContainerIsNotPresentIsNotAdded(String dependencyId, String docHref) {
		assertHelpDocument("3.0.0", dependencyId).doesNotContain("https://www.testcontainers.org/modules/" + docHref);
	}

	static Stream<Arguments> supportedEntriesHelpDocument() {
		return Stream.of(Arguments.arguments("amqp", "rabbitmq/"),
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
				Arguments.arguments("pulsar-reactive", "pulsar/"),
				Arguments.arguments("sqlserver", "databases/mssqlserver/"));
	}

	@Test
	void linkToSupportedEntriesWhenTwoMatchesArePresentOnlyAddLinkOnce() {
		assertHelpDocument("3.0.0", "testcontainers", "data-mongodb", "data-mongodb-reactive")
			.containsOnlyOnce("https://www.testcontainers.org/modules/databases/mongodb/");
	}

	@Test
	void buildWithSpringBoot31DoesNotIncludeBom() {
		assertThat(generateProject("3.1.0-RC1", "testcontainers")).mavenBuild()
			.doesNotHaveBom("org.testcontainers", "testcontainers-bom")
			.hasDependency(getDependency("testcontainers"));
	}

	@Test
	void buildWithSpringBoot30DoesNotIncludeSpringBootTestcontainers() {
		assertThat(generateProject("3.0.0", "testcontainers")).mavenBuild()
			.doesNotHaveDependency("org.springframework.boot", "spring-boot-testcontainers");
	}

	@Test
	void buildWithSpringBoot31IncludeSpringBootTestcontainers() {
		assertThat(generateProject("3.1.0-RC1", "testcontainers")).mavenBuild()
			.hasDependency("org.springframework.boot", "spring-boot-testcontainers", null, "test");
	}

	@Test
	void buildWithSpringBoot30DoesNotIncludeTestcontainersSection() {
		assertHelpDocument("3.0.0", "testcontainers").doesNotContain("Spring Boot Testcontainers support");
	}

	@Test
	void buildWithSpringBoot31IncludeTestcontainersSection() {
		assertHelpDocument("3.1.0-RC1", "testcontainers").contains("Spring Boot Testcontainers support");
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
