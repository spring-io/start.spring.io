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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on Testcontainers.
 *
 * @author Maciej Walkowiak
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
public class TestcontainersProjectGenerationConfiguration {

	private final List<TestContainersModule> testContainersModules;

	public TestcontainersProjectGenerationConfiguration() {
		this.testContainersModules = createTestContainersModules();
	}

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	public TestcontainersBuildCustomizer testContainersBuildCustomizer(InitializrMetadata metadata,
			ProjectDescription description) {
		return new TestcontainersBuildCustomizer(this.testContainersModules, metadata, description);
	}

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	public TestcontainersHelpCustomizer testcontainersHelpCustomizer(Build build) {
		return new TestcontainersHelpCustomizer(this.testContainersModules, build);
	}

	private static List<TestContainersModule> createTestContainersModules() {
		List<TestContainersModule> modules = new ArrayList<>();
		modules.add(new TestContainersModule("amqp", addDependency("rabbitmq"),
				addReferenceLink("rabbitmq/", "RabbitMQ Module")));
		modules.add(new TestContainersModule(Arrays.asList("data-cassandra", "data-cassandra-reactive"),
				addDependency("cassandra"), addReferenceLink("databases/cassandra/", "Cassandra Module")));
		modules.add(new TestContainersModule(Arrays.asList("data-couchbase", "data-couchbase-reactive"),
				addDependency("couchbase"), addReferenceLink("databases/couchbase/", "Couchbase Module")));
		modules.add(new TestContainersModule("data-elasticsearch", addDependency("elasticsearch"),
				addReferenceLink("elasticsearch/", "Elasticsearch Container")));
		modules.add(new TestContainersModule(Arrays.asList("data-mongodb", "data-mongodb-reactive"),
				addDependency("mongodb"), addReferenceLink("databases/mongodb/", "MongoDB Module")));
		modules.add(new TestContainersModule("data-neo4j", addDependency("neo4j"),
				addReferenceLink("databases/neo4j/", "Neo4j Module")));
		modules.add(new TestContainersModule("data-r2dbc", addDependency("r2dbc"),
				addReferenceLink("databases/r2dbc/", "R2DBC support")));
		modules.add(new TestContainersModule("data-solr", addDependency("solr"),
				addReferenceLink("solr/", "Solr Container")));
		modules.add(new TestContainersModule("db2", addDependency("db2"),
				addReferenceLink("databases/db2/", "DB2 Module")));
		modules.add(new TestContainersModule(Arrays.asList("kafka", "kafka-streams"), addDependency("kafka"),
				addReferenceLink("kafka/", "Kafka Modules")));
		modules.add(new TestContainersModule("mysql", addDependency("mysql"),
				addReferenceLink("databases/mysql/", "MySQL Module")));
		modules.add(new TestContainersModule("postgresql", addDependency("postgresql"),
				addReferenceLink("databases/postgres/", "Postgres Module")));
		modules.add(new TestContainersModule("oracle", addDependency("oracle-xe"),
				addReferenceLink("databases/oraclexe/", "Oracle-XE Module")));
		modules.add(new TestContainersModule("sqlserver", addDependency("mssqlserver"),
				addReferenceLink("databases/mssqlserver/", "MS SQL Server Module")));
		return modules;
	}

	private static Consumer<Build> addDependency(String id) {
		return (build) -> build.dependencies().add("testcontainers-" + id,
				Dependency.withCoordinates("org.testcontainers", id).scope(DependencyScope.TEST_COMPILE));
	}

	private static Consumer<HelpDocument> addReferenceLink(String href, String moduleName) {
		return (document) -> document.gettingStarted().addReferenceDocLink(
				String.format("https://www.testcontainers.org/modules/%s", href),
				String.format("Testcontainers %s Reference Guide", moduleName));
	}

}
