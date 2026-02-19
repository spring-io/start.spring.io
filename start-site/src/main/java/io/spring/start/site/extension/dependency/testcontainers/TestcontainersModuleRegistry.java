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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.start.site.container.Testcontainers;
import io.spring.start.site.container.Testcontainers.Container;
import io.spring.start.site.container.Testcontainers.SupportedContainer;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependency.Builder;

/**
 * A registry of available Testcontainers modules.
 *
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 * @author Chris Bono
 * @author Moritz Halbritter
 */
abstract class TestcontainersModuleRegistry {

	static Iterable<ImplicitDependency> create(Testcontainers testcontainers) {
		List<ImplicitDependency.Builder> builders = new ArrayList<>();
		builders.add(onDependencies("activemq")
			.customizeBuild(addModule("activemq", testcontainers, SupportedContainer.ACTIVEMQ))
			.customizeHelpDocument(addReferenceLink("ActiveMQ Module", "activemq/")));
		builders.add(onDependencies("artemis")
			.customizeBuild(addModule("activemq", testcontainers, SupportedContainer.ARTEMIS))
			.customizeHelpDocument(addReferenceLink("ActiveMQ Module", "activemq/")));
		builders.add(onDependencies("amqp", "amqp-streams")
			.customizeBuild(addModule("rabbitmq", testcontainers, SupportedContainer.RABBITMQ))
			.customizeHelpDocument(addReferenceLink("RabbitMQ Module", "rabbitmq/")));
		builders.add(onDependencies("cloud-gcp", "cloud-gcp-pubsub").customizeBuild(addModule("gcloud", testcontainers))
			.customizeHelpDocument(addReferenceLink("GCloud Module", "gcloud/")));
		builders.add(onDependencies("cloud-starter-consul-config").customizeBuild(addModule("consul", testcontainers))
			.customizeHelpDocument(addReferenceLink("Consul Module", "consul/")));
		builders.add(onDependencies("cloud-starter-vault-config").customizeBuild(addModule("vault", testcontainers))
			.customizeHelpDocument(addReferenceLink("Vault Module", "vault/")));
		builders.add(onDependencies("data-cassandra", "data-cassandra-reactive", "spring-ai-vectordb-cassandra")
			.customizeBuild(addModule("cassandra", testcontainers, SupportedContainer.CASSANDRA))
			.customizeHelpDocument(addReferenceLink("Cassandra Module", "databases/cassandra/")));
		builders.add(onDependencies("data-couchbase", "data-couchbase-reactive")
			.customizeBuild(addModule("couchbase", testcontainers))
			.customizeHelpDocument(addReferenceLink("Couchbase Module", "databases/couchbase/")));
		builders.add(onDependencies("data-elasticsearch", "spring-ai-vectordb-elasticsearch")
			.customizeBuild(addModule("elasticsearch", testcontainers, SupportedContainer.ELASTICSEARCH))
			.customizeHelpDocument(addReferenceLink("Elasticsearch Container", "elasticsearch/")));
		builders.add(onDependencies("data-mongodb", "data-mongodb-reactive", "spring-ai-vectordb-mongodb-atlas")
			.customizeBuild(addModule("mongodb", testcontainers, SupportedContainer.MONGODB))
			.customizeHelpDocument(addReferenceLink("MongoDB Module", "databases/mongodb/")));
		builders.add(onDependencies("data-neo4j", "spring-ai-vectordb-neo4j")
			.customizeBuild(addModule("neo4j", testcontainers, SupportedContainer.NEO4J))
			.customizeHelpDocument(addReferenceLink("Neo4j Module", "databases/neo4j/")));
		builders.add(onDependencies("opentelemetry")
			.customizeBuild(addModule("grafana-lgtm", testcontainers, SupportedContainer.GRAFANA_LGTM))
			.customizeHelpDocument(addReferenceLink("Grafana Module", "grafana/")));
		builders.add(onDependencies("data-r2dbc").customizeBuild(addModule("r2dbc", testcontainers))
			.customizeHelpDocument(addReferenceLink("R2DBC support", "databases/r2dbc/")));
		builders.add(onDependencies("db2").customizeBuild(addModule("db2", testcontainers))
			.customizeHelpDocument(addReferenceLink("DB2 Module", "databases/db2/")));
		builders.add(onDependencies("kafka", "kafka-streams")
			.customizeBuild(addModule("kafka", testcontainers, SupportedContainer.KAFKA))
			.customizeHelpDocument(addReferenceLink("Kafka Modules", "kafka/")));
		builders.add(onDependencies("mariadb", "spring-ai-vectordb-mariadb")
			.customizeBuild(addModule("mariadb", testcontainers, SupportedContainer.MARIADB))
			.customizeHelpDocument(addReferenceLink("MariaDB Module", "databases/mariadb/")));
		builders
			.add(onDependencies("mysql").customizeBuild(addModule("mysql", testcontainers, SupportedContainer.MYSQL))
				.customizeHelpDocument(addReferenceLink("MySQL Module", "databases/mysql/")));
		builders.add(onDependencies("oracle", "spring-ai-vectordb-oracle")
			.customizeBuild(addModule("oracle-free", testcontainers, SupportedContainer.ORACLE))
			.customizeHelpDocument(addReferenceLink("Oracle-Free Module", "databases/oraclefree/")));
		builders.add(onDependencies("postgresql", "spring-ai-vectordb-pgvector")
			.customizeBuild(addModule("postgresql", testcontainers, SupportedContainer.POSTGRESQL))
			.customizeHelpDocument(addReferenceLink("Postgres Module", "databases/postgres/")));
		builders.add(onDependencies("pulsar", "pulsar-reactive")
			.customizeBuild(addModule("pulsar", testcontainers, SupportedContainer.PULSAR))
			.customizeHelpDocument(addReferenceLink("Pulsar Module", "pulsar/")));
		builders.add(onDependencies("solace").customizeBuild(addModule("solace", testcontainers))
			.customizeHelpDocument(addReferenceLink("Solace Module", "solace/")));
		builders.add(onDependencies("sqlserver")
			.customizeBuild(addModule("mssqlserver", testcontainers, SupportedContainer.MSSQL))
			.customizeHelpDocument(addReferenceLink("MS SQL Server Module", "databases/mssqlserver/")));
		builders.add(onDependencies("spring-ai-vectordb-chroma")
			.customizeBuild(addModule("chromadb", testcontainers, SupportedContainer.CHROMADB))
			.customizeHelpDocument(addReferenceLink("Chroma Module", "testcontainers/")));
		builders.add(onDependencies("spring-ai-vectordb-milvus")
			.customizeBuild(addModule("milvus", testcontainers, SupportedContainer.MILVUS))
			.customizeHelpDocument(addReferenceLink("Milvus Module", "testcontainers/")));
		builders.add(onDependencies("spring-ai-ollama")
			.customizeBuild(addModule("ollama", testcontainers, SupportedContainer.OLLAMA))
			.customizeHelpDocument(addReferenceLink("Ollama Module", "testcontainers/")));
		builders.add(onDependencies("spring-ai-vectordb-qdrant")
			.customizeBuild(addModule("qdrant", testcontainers, SupportedContainer.QDRANT))
			.customizeHelpDocument(addReferenceLink("Qdrant Module", "testcontainers/")));
		builders.add(onDependencies("spring-ai-vectordb-weaviate")
			.customizeBuild(addModule("weaviate", testcontainers, SupportedContainer.WEAVIATE))
			.customizeHelpDocument(addReferenceLink("Weaviate Module", "testcontainers/")));
		return builders.stream().map(Builder::build).toList();
	}

	private static ImplicitDependency.Builder onDependencies(String... dependencyIds) {
		return new Builder().matchAnyDependencyIds(dependencyIds);
	}

	private static Consumer<Build> addModule(String id, Testcontainers testcontainers) {
		return (build) -> build.dependencies()
			.add("testcontainers-" + id,
					Dependency.withCoordinates("org.testcontainers", testcontainers.resolveArtifactId(id))
						.scope(DependencyScope.TEST_COMPILE));
	}

	private static Consumer<Build> addModule(String id, Testcontainers testcontainers,
			Testcontainers.SupportedContainer supportedContainer) {
		Container container = testcontainers.getContainer(supportedContainer);
		return (build) -> build.dependencies()
			.add("testcontainers-" + id, Dependency.withCoordinates("org.testcontainers", container.module())
				.scope(DependencyScope.TEST_COMPILE));
	}

	private static Consumer<HelpDocument> addReferenceLink(String name, String modulePath) {
		return (helpDocument) -> {
			String href = String.format("https://java.testcontainers.org/modules/%s", modulePath);
			String description = String.format("Testcontainers %s Reference Guide", name);
			helpDocument.gettingStarted().addReferenceDocLink(href, description);
		};
	}

}
