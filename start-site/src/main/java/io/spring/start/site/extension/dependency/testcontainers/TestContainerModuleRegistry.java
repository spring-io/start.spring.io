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

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;

/**
 * A registry of available {@link TestContainersModule Testcontainers modules}.
 *
 * @author Stephane Nicoll
 */
public final class TestContainerModuleRegistry {

	private final List<TestContainersModule> modules;

	private TestContainerModuleRegistry(List<TestContainersModule> modules) {
		this.modules = modules;
	}

	static TestContainerModuleRegistry create(TestContainersModule... modules) {
		return new TestContainerModuleRegistry(Arrays.asList(modules));
	}

	static TestContainerModuleRegistry create() {
		return create(
				new TestContainersModule("RabbitMQ Module", referenceLink("rabbitmq/"), "amqp",
						addDependency("rabbitmq")),
				new TestContainersModule("Cassandra Module", referenceLink("databases/cassandra/"),
						Arrays.asList("data-cassandra", "data-cassandra-reactive"), addDependency("cassandra")),
				new TestContainersModule("Couchbase Module", referenceLink("databases/couchbase/"),
						Arrays.asList("data-couchbase", "data-couchbase-reactive"), addDependency("couchbase")),
				new TestContainersModule("Elasticsearch Container", referenceLink("elasticsearch/"),
						"data-elasticsearch", addDependency("elasticsearch")),
				new TestContainersModule("MongoDB Module", referenceLink("databases/mongodb/"),
						Arrays.asList("data-mongodb", "data-mongodb-reactive"), addDependency("mongodb")),
				new TestContainersModule("Neo4j Module", referenceLink("databases/neo4j/"), "data-neo4j",
						addDependency("neo4j")),
				new TestContainersModule("R2DBC support", referenceLink("databases/r2dbc/"), "data-r2dbc",
						addDependency("r2dbc")),
				new TestContainersModule("Solr Container", referenceLink("solr/"), "data-solr", addDependency("solr")),
				new TestContainersModule("DB2 Module", referenceLink("databases/db2/"), "db2", addDependency("db2")),
				new TestContainersModule("Kafka Modules", referenceLink("kafka/"),
						Arrays.asList("kafka", "kafka-streams"), addDependency("kafka")),
				new TestContainersModule("MariaDB Module", referenceLink("databases/mariadb/"), "mariadb",
						addDependency("mariadb")),
				new TestContainersModule("MySQL Module", referenceLink("databases/mysql/"), "mysql",
						addDependency("mysql")),
				new TestContainersModule("Postgres Module", referenceLink("databases/postgres/"), "postgresql",
						addDependency("postgresql")),
				new TestContainersModule("Oracle-XE Module", referenceLink("databases/oraclexe/"), "oracle",
						addDependency("oracle-xe")),
				new TestContainersModule("MS SQL Server Module", referenceLink("databases/mssqlserver/"), "sqlserver",
						addDependency("mssqlserver")));
	}

	private static Consumer<Build> addDependency(String id) {
		return (build) -> build.dependencies().add("testcontainers-" + id,
				Dependency.withCoordinates("org.testcontainers", id).scope(DependencyScope.TEST_COMPILE));
	}

	private static String referenceLink(String href) {
		return String.format("https://www.testcontainers.org/modules/%s", href);
	}

	Stream<TestContainersModule> modules() {
		return this.modules.stream();
	}

}
