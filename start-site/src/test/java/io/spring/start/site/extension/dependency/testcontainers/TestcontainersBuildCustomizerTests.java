/*
 * Copyright 2012-2020 the original author or authors.
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

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TestcontainersBuildCustomizer}.
 *
 * @author Maciej Walkowiak
 */
class TestcontainersBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void onlyTestContainersWithoutDrivers() {
		assertThat(generateProject("testcontainers")).mavenBuild()
				.hasBom("org.testcontainers", "testcontainers-bom", "${testcontainers.version}")
				.hasDependency(getDependency("testcontainers"));
	}

	@ParameterizedTest
	@MethodSource("rdbmsDependencies")
	void testcontainersWithJdbcDatabaseDriver(String springBootDependencyId, String testcontainersArtifactId) {
		Dependency testcontainers = getDependency("testcontainers");

		assertThat(generateProject("testcontainers", springBootDependencyId)).mavenBuild()
				.hasBom("org.testcontainers", "testcontainers-bom", "${testcontainers.version}")
				.hasDependency(getDependency(springBootDependencyId))
				.hasDependency("org.testcontainers", testcontainersArtifactId, null, "test")
				.doesNotHaveDependency(testcontainers.getGroupId(), testcontainers.getArtifactId())
				.doesNotHaveDependency("org.testcontainers", "r2dbc");
	}

	@ParameterizedTest
	@MethodSource("noSqlDependencies")
	void testcontainersWithNoSqlWithoutR2dbc(String springBootDependencyId, String testcontainersArtifactId) {
		Dependency testcontainers = getDependency("testcontainers");

		assertThat(generateProject("testcontainers", springBootDependencyId)).mavenBuild()
				.hasBom("org.testcontainers", "testcontainers-bom", "${testcontainers.version}")
				.hasDependency(getDependency(springBootDependencyId))
				.hasDependency("org.testcontainers", testcontainersArtifactId, null, "test")
				.doesNotHaveDependency(testcontainers.getGroupId(), testcontainers.getArtifactId())
				.doesNotHaveDependency("org.testcontainers", "r2dbc");
	}

	@ParameterizedTest
	@MethodSource("noSqlDependencies")
	void testcontainersWithNoSqlWithR2dbc(String springBootDependencyId, String testcontainersArtifactId) {
		Dependency testcontainers = getDependency("testcontainers");

		assertThat(generateProject("testcontainers", "data-r2dbc", springBootDependencyId)).mavenBuild()
				.hasBom("org.testcontainers", "testcontainers-bom", "${testcontainers.version}")
				.hasDependency(getDependency(springBootDependencyId))
				.hasDependency("org.testcontainers", testcontainersArtifactId, null, "test")
				.doesNotHaveDependency(testcontainers.getGroupId(), testcontainers.getArtifactId())
				.doesNotHaveDependency("org.testcontainers", "r2dbc");
	}

	@ParameterizedTest
	@MethodSource("rdbmsDependencies")
	void testcontainersWithR2dbcDatabaseDriver(String springBootDependencyId, String testcontainersArtifactId) {
		Dependency testcontainers = getDependency("testcontainers");

		assertThat(generateProject("testcontainers", "data-r2dbc", springBootDependencyId)).mavenBuild()
				.hasBom("org.testcontainers", "testcontainers-bom", "${testcontainers.version}")
				.hasDependency(getDependency(springBootDependencyId))
				.hasDependency("org.testcontainers", "r2dbc", null, "test")
				.hasDependency("org.testcontainers", testcontainersArtifactId, null, "test")
				.doesNotHaveDependency(testcontainers.getGroupId(), testcontainers.getArtifactId());
	}

	@Test
	void testcontainersWithR2dbcNoDatabaseDriver() {
		Dependency testcontainers = getDependency("testcontainers");

		assertThat(generateProject("testcontainers", "data-r2dbc")).mavenBuild()
				.hasBom("org.testcontainers", "testcontainers-bom", "${testcontainers.version}")
				.hasDependency(getDependency("testcontainers")).doesNotHaveDependency("org.testcontainers", "r2dbc");
	}

	private ProjectStructure generateProject(String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setBootVersion("2.3.0.BUILD-SNAPSHOT");
		request.setType("maven-build");
		return generateProject(request);
	}

	static Stream<Arguments> rdbmsDependencies() {
		return Stream.of(Arguments.arguments("postgresql", "postgresql"), Arguments.arguments("mysql", "mysql"),
				Arguments.arguments("sqlserver", "mssqlserver"), Arguments.arguments("oracle", "oracle-xe"));
	}

	static Stream<Arguments> noSqlDependencies() {
		return Stream.of(Arguments.arguments("data-neo4j", "neo4j"), Arguments.arguments("data-cassandra", "cassandra"),
				Arguments.arguments("data-cassandra-reactive", "cassandra"),
				Arguments.arguments("data-couchbase", "couchbase"),
				Arguments.arguments("data-couchbase-reactive", "couchbase"));
	}

}
