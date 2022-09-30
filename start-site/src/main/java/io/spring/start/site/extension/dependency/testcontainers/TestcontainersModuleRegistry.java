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

package io.spring.start.site.extension.dependency.testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependency.Builder;

/**
 * A registry of available Testcontainers modules.
 *
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
abstract class TestcontainersModuleRegistry {

	static Iterable<ImplicitDependency> create() {
		return create(
				onDependencies("amqp").customizeBuild(addModule("rabbitmq"))
						.customizeHelpDocument(addReferenceLink("RabbitMQ Module", "rabbitmq/")),
				onDependencies("cloud-starter-consul-config").customizeBuild(addModule("consul"))
						.customizeHelpDocument(addReferenceLink("Consul Module", "consul/")),
				onDependencies("cloud-starter-vault-config").customizeBuild(addModule("vault"))
						.customizeHelpDocument(addReferenceLink("Vault Module", "vault/")),
				onDependencies("data-cassandra", "data-cassandra-reactive").customizeBuild(addModule("cassandra"))
						.customizeHelpDocument(addReferenceLink("Cassandra Module", "databases/cassandra/")),
				onDependencies("data-couchbase", "data-couchbase-reactive").customizeBuild(addModule("couchbase"))
						.customizeHelpDocument(addReferenceLink("Couchbase Module", "databases/couchbase/")),
				onDependencies("data-elasticsearch").customizeBuild(addModule("elasticsearch"))
						.customizeHelpDocument(addReferenceLink("Elasticsearch Container", "elasticsearch/")),
				onDependencies("data-mongodb", "data-mongodb-reactive").customizeBuild(addModule("mongodb"))
						.customizeHelpDocument(addReferenceLink("MongoDB Module", "databases/mongodb/")),
				onDependencies("data-neo4j").customizeBuild(addModule("neo4j"))
						.customizeHelpDocument(addReferenceLink("Neo4j Module", "databases/neo4j/")),
				onDependencies("data-r2dbc").customizeBuild(addModule("r2dbc"))
						.customizeHelpDocument(addReferenceLink("R2DBC support", "databases/r2dbc/")),
				onDependencies("db2").customizeBuild(addModule("db2"))
						.customizeHelpDocument(addReferenceLink("DB2 Module", "databases/db2/")),
				onDependencies("kafka", "kafka-streams").customizeBuild(addModule("kafka"))
						.customizeHelpDocument(addReferenceLink("Kafka Modules", "kafka/")),
				onDependencies("mariadb").customizeBuild(addModule("mariadb"))
						.customizeHelpDocument(addReferenceLink("MariaDB Module", "databases/mariadb/")),
				onDependencies("mysql").customizeBuild(addModule("mysql"))
						.customizeHelpDocument(addReferenceLink("MySQL Module", "databases/mysql/")),
				onDependencies("postgresql").customizeBuild(addModule("postgresql"))
						.customizeHelpDocument(addReferenceLink("Postgres Module", "databases/postgres/")),
				onDependencies("oracle").customizeBuild(addModule("oracle-xe"))
						.customizeHelpDocument(addReferenceLink("Oracle-XE Module", "databases/oraclexe/")),
				onDependencies("sqlserver").customizeBuild(addModule("mssqlserver"))
						.customizeHelpDocument(addReferenceLink("MS SQL Server Module", "databases/mssqlserver/")));
	}

	private static List<ImplicitDependency> create(ImplicitDependency.Builder... dependencies) {
		return Arrays.stream(dependencies).map(Builder::build).collect(Collectors.toList());
	}

	private static ImplicitDependency.Builder onDependencies(String... dependencyIds) {
		return new Builder().matchAnyDependencyIds(dependencyIds);
	}

	private static Consumer<Build> addModule(String id) {
		return (build) -> build.dependencies().add("testcontainers-" + id,
				Dependency.withCoordinates("org.testcontainers", id).scope(DependencyScope.TEST_COMPILE));
	}

	private static Consumer<HelpDocument> addReferenceLink(String name, String modulePath) {
		return (helpDocument) -> {
			String href = String.format("https://www.testcontainers.org/modules/%s", modulePath);
			String description = String.format("Testcontainers %s Reference Guide", name);
			helpDocument.gettingStarted().addReferenceDocLink(href, description);
		};
	}

}
