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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependency.Builder;

/**
 * A registry of available Testcontainers modules.
 *
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 * @author Chris Bono
 */
abstract class TestcontainersModuleRegistry {

	private static final VersionRange SPRING_BOOT_3_2_0_OR_LATER = VersionParser.DEFAULT.parseRange("3.2.0");

	static Iterable<ImplicitDependency> create(Version platformVersion) {
		List<ImplicitDependency.Builder> builders = new ArrayList<>();
		builders.add(onDependencies("amqp").customizeBuild(addModule("rabbitmq"))
			.customizeHelpDocument(addReferenceLink("RabbitMQ Module", "rabbitmq/")));
		builders.add(onDependencies("cloud-gcp", "cloud-gcp-pubsub").customizeBuild(addModule("gcloud"))
			.customizeHelpDocument(addReferenceLink("GCloud Module", "gcloud/")));
		builders.add(onDependencies("cloud-starter-consul-config").customizeBuild(addModule("consul"))
			.customizeHelpDocument(addReferenceLink("Consul Module", "consul/")));
		builders.add(onDependencies("cloud-starter-vault-config").customizeBuild(addModule("vault"))
			.customizeHelpDocument(addReferenceLink("Vault Module", "vault/")));
		builders.add(onDependencies("data-cassandra", "data-cassandra-reactive").customizeBuild(addModule("cassandra"))
			.customizeHelpDocument(addReferenceLink("Cassandra Module", "databases/cassandra/")));
		builders.add(onDependencies("data-couchbase", "data-couchbase-reactive").customizeBuild(addModule("couchbase"))
			.customizeHelpDocument(addReferenceLink("Couchbase Module", "databases/couchbase/")));
		builders.add(onDependencies("data-elasticsearch").customizeBuild(addModule("elasticsearch"))
			.customizeHelpDocument(addReferenceLink("Elasticsearch Container", "elasticsearch/")));
		builders.add(onDependencies("data-mongodb", "data-mongodb-reactive").customizeBuild(addModule("mongodb"))
			.customizeHelpDocument(addReferenceLink("MongoDB Module", "databases/mongodb/")));
		builders.add(onDependencies("data-neo4j").customizeBuild(addModule("neo4j"))
			.customizeHelpDocument(addReferenceLink("Neo4j Module", "databases/neo4j/")));
		builders.add(onDependencies("data-r2dbc").customizeBuild(addModule("r2dbc"))
			.customizeHelpDocument(addReferenceLink("R2DBC support", "databases/r2dbc/")));
		builders.add(onDependencies("db2").customizeBuild(addModule("db2"))
			.customizeHelpDocument(addReferenceLink("DB2 Module", "databases/db2/")));
		builders.add(onDependencies("kafka", "kafka-streams").customizeBuild(addModule("kafka"))
			.customizeHelpDocument(addReferenceLink("Kafka Modules", "kafka/")));
		builders.add(onDependencies("mariadb").customizeBuild(addModule("mariadb"))
			.customizeHelpDocument(addReferenceLink("MariaDB Module", "databases/mariadb/")));
		builders.add(onDependencies("mysql").customizeBuild(addModule("mysql"))
			.customizeHelpDocument(addReferenceLink("MySQL Module", "databases/mysql/")));
		if (SPRING_BOOT_3_2_0_OR_LATER.match(platformVersion)) {
			builders.add(onDependencies("oracle").customizeBuild(addModule("oracle-free"))
				.customizeHelpDocument(addReferenceLink("Oracle-Free Module", "databases/oraclefree/")));
		}
		else {
			builders.add(onDependencies("oracle").customizeBuild(addModule("oracle-xe"))
				.customizeHelpDocument(addReferenceLink("Oracle-XE Module", "databases/oraclexe/")));
		}
		builders.add(onDependencies("postgresql").customizeBuild(addModule("postgresql"))
			.customizeHelpDocument(addReferenceLink("Postgres Module", "databases/postgres/")));
		builders.add(onDependencies("pulsar", "pulsar-reactive").customizeBuild(addModule("pulsar"))
			.customizeHelpDocument(addReferenceLink("Pulsar Module", "pulsar/")));
		builders.add(onDependencies("solace").customizeBuild(addModule("solace"))
			.customizeHelpDocument(addReferenceLink("Solace Module", "solace/")));
		builders.add(onDependencies("sqlserver").customizeBuild(addModule("mssqlserver"))
			.customizeHelpDocument(addReferenceLink("MS SQL Server Module", "databases/mssqlserver/")));
		return builders.stream().map(Builder::build).collect(Collectors.toList());
	}

	private static ImplicitDependency.Builder onDependencies(String... dependencyIds) {
		return new Builder().matchAnyDependencyIds(dependencyIds);
	}

	private static Consumer<Build> addModule(String id) {
		return (build) -> build.dependencies()
			.add("testcontainers-" + id,
					Dependency.withCoordinates("org.testcontainers", id).scope(DependencyScope.TEST_COMPILE));
	}

	private static Consumer<HelpDocument> addReferenceLink(String name, String modulePath) {
		return (helpDocument) -> {
			String href = String.format("https://java.testcontainers.org/modules/%s", modulePath);
			String description = String.format("Testcontainers %s Reference Guide", name);
			helpDocument.gettingStarted().addReferenceDocLink(href, description);
		};
	}

}
