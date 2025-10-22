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

package io.spring.start.site.container;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Manages testcontainers with their container names and modules.
 *
 * @author Moritz Halbritter
 */
public class Testcontainers {

	/**
	 * Class name of the generic container.
	 */
	public static final String GENERIC_CONTAINER_CLASS_NAME = "org.testcontainers.containers.GenericContainer";

	// 4.0.0-RC1 has Testcontainers 2.0+
	private static final VersionRange SPRING_BOOT_4_RC1_OR_LATER = VersionParser.DEFAULT.parseRange("4.0.0-RC1");

	private final Version bootVersion;

	public Testcontainers(Version bootVersion) {
		this.bootVersion = bootVersion;
	}

	/**
	 * Returns the container details for the given supported container.
	 * @param supportedContainer the supported container
	 * @return the container details
	 */
	public Container getContainer(SupportedContainer supportedContainer) {
		return switch (supportedContainer) {
			case ACTIVEMQ -> (hasTc2())
					? Container.of("org.testcontainers.activemq.ActiveMQContainer", false, "testcontainers-activemq")
					: Container.of("org.testcontainers.activemq.ActiveMQContainer", false, "activemq");
			case ARTEMIS -> (hasTc2())
					? Container.of("org.testcontainers.activemq.ArtemisContainer", false, "testcontainers-activemq")
					: Container.of("org.testcontainers.activemq.ArtemisContainer", false, "activemq");
			case CASSANDRA -> (hasTc2())
					? Container.of("org.testcontainers.cassandra.CassandraContainer", false, "testcontainers-cassandra")
					: Container.of("org.testcontainers.cassandra.CassandraContainer", false, "cassandra");
			case CHROMADB -> (hasTc2())
					? Container.of("org.testcontainers.chromadb.ChromaDBContainer", false, "testcontainers-chromadb")
					: Container.of("org.testcontainers.chromadb.ChromaDBContainer", false, "chromadb");
			case ELASTICSEARCH -> (hasTc2())
					? Container.of("org.testcontainers.elasticsearch.ElasticsearchContainer", false,
							"testcontainers-elasticsearch")
					: Container.of("org.testcontainers.elasticsearch.ElasticsearchContainer", false, "elasticsearch");
			case GENERIC -> Container.of(GENERIC_CONTAINER_CLASS_NAME, true, null);
			case GRAFANA_LGTM -> (hasTc2())
					? Container.of("org.testcontainers.grafana.LgtmStackContainer", false, "testcontainers-grafana")
					: Container.of("org.testcontainers.grafana.LgtmStackContainer", false, "grafana");
			case KAFKA ->
				(hasTc2()) ? Container.of("org.testcontainers.kafka.KafkaContainer", false, "testcontainers-kafka")
						: Container.of("org.testcontainers.kafka.KafkaContainer", false, "kafka");
			case MARIADB -> (hasTc2())
					? Container.of("org.testcontainers.mariadb.MariaDBContainer", false, "testcontainers-mariadb")
					: Container.of("org.testcontainers.containers.MariaDBContainer", true, "mariadb");
			case MILVUS ->
				(hasTc2()) ? Container.of("org.testcontainers.milvus.MilvusContainer", false, "testcontainers-milvus")
						: Container.of("org.testcontainers.milvus.MilvusContainer", false, "milvus");
			case MONGODB -> (hasTc2())
					? Container.of("org.testcontainers.mongodb.MongoDBContainer", false, "testcontainers-mongodb")
					: Container.of("org.testcontainers.containers.MongoDBContainer", false, "mongodb");
			case MONGODB_ATLAS -> (hasTc2())
					? Container.of("org.testcontainers.mongodb.MongoDBAtlasLocalContainer", false,
							"testcontainers-mongodb")
					: Container.of("org.testcontainers.mongodb.MongoDBAtlasLocalContainer", false, "mongodb");
			case MSSQL -> (hasTc2())
					? Container.of("org.testcontainers.mssqlserver.MSSQLServerContainer", false,
							"testcontainers-mssqlserver")
					: Container.of("org.testcontainers.containers.MSSQLServerContainer", true, "mssqlserver");
			case MYSQL ->
				(hasTc2()) ? Container.of("org.testcontainers.mysql.MySQLContainer", false, "testcontainers-mysql")
						: Container.of("org.testcontainers.containers.MySQLContainer", true, "mysql");
			case NEO4J ->
				(hasTc2()) ? Container.of("org.testcontainers.neo4j.Neo4jContainer", false, "testcontainers-neo4j")
						: Container.of("org.testcontainers.containers.Neo4jContainer", true, "neo4j");
			case OLLAMA ->
				hasTc2() ? Container.of("org.testcontainers.ollama.OllamaContainer", false, "testcontainers-ollama")
						: Container.of("org.testcontainers.ollama.OllamaContainer", false, "ollama");
			case ORACLE -> (hasTc2())
					? Container.of("org.testcontainers.oracle.OracleContainer", false, "testcontainers-oracle-free")
					: Container.of("org.testcontainers.oracle.OracleContainer", false, "oracle-free");
			case POSTGRESQL -> (hasTc2())
					? Container.of("org.testcontainers.postgresql.PostgreSQLContainer", false,
							"testcontainers-postgresql")
					: Container.of("org.testcontainers.containers.PostgreSQLContainer", true, "postgresql");
			case PULSAR ->
				(hasTc2()) ? Container.of("org.testcontainers.pulsar.PulsarContainer", false, "testcontainers-pulsar")
						: Container.of("org.testcontainers.containers.PulsarContainer", false, "pulsar");
			case QDRANT ->
				(hasTc2()) ? Container.of("org.testcontainers.qdrant.QdrantContainer", false, "testcontainers-qdrant")
						: Container.of("org.testcontainers.qdrant.QdrantContainer", false, "qdrant");
			case RABBITMQ -> (hasTc2())
					? Container.of("org.testcontainers.rabbitmq.RabbitMQContainer", false, "testcontainers-rabbitmq")
					: Container.of("org.testcontainers.containers.RabbitMQContainer", false, "rabbitmq");
			case WEAVIATE -> (hasTc2())
					? Container.of("org.testcontainers.weaviate.WeaviateContainer", false, "testcontainers-weaviate")
					: Container.of("org.testcontainers.weaviate.WeaviateContainer", false, "weaviate");
		};
	}

	/**
	 * Resolves the artifact id for the given module.
	 * @param module the module
	 * @return the artifact id
	 */
	public String resolveArtifactId(String module) {
		return (hasTc2()) ? "testcontainers-" + module : module;
	}

	boolean hasTc2() {
		return SPRING_BOOT_4_RC1_OR_LATER.match(this.bootVersion);
	}

	/**
	 * Supported containers.
	 */
	public enum SupportedContainer {

		/**
		 * ActiveMQ.
		 */
		ACTIVEMQ,
		/**
		 * ActiveMQ Artemis.
		 */
		ARTEMIS,
		/**
		 * Cassandra.
		 */
		CASSANDRA,
		/**
		 * Chroma DB.
		 */
		CHROMADB,
		/**
		 * ElasticSearch.
		 */
		ELASTICSEARCH,
		/**
		 * Generic container.
		 */
		GENERIC,
		/**
		 * Grafana LGTM stack.
		 */
		GRAFANA_LGTM,
		/**
		 * Apache Kafka.
		 */
		KAFKA,
		/**
		 * MariaDB.
		 */
		MARIADB,
		/**
		 * Milvus.
		 */
		MILVUS,
		/**
		 * MongoDB.
		 */
		MONGODB,
		/**
		 * MongoDB Atlas.
		 */
		MONGODB_ATLAS,
		/**
		 * SQL Server.
		 */
		MSSQL,
		/**
		 * MySQL.
		 */
		MYSQL,
		/**
		 * Neo4J.
		 */
		NEO4J,
		/**
		 * Ollama.
		 */
		OLLAMA,
		/**
		 * Oracle.
		 */
		ORACLE,
		/**
		 * PostgreSQL.
		 */
		POSTGRESQL,
		/**
		 * Pulsar.
		 */
		PULSAR,
		/**
		 * Qdrant.
		 */
		QDRANT,
		/**
		 * RabbitMQ.
		 */
		RABBITMQ,
		/**
		 * Weaviate.
		 */
		WEAVIATE

	}

	/**
	 * Container details.
	 *
	 * @param className the class name
	 * @param generic whether the class is generic
	 * @param module the module in which the class resides in
	 */
	public record Container(String className, boolean generic, String module) {
		static Container of(String className, boolean generic, String module) {
			return new Container(className, generic, module);
		}
	}

}
