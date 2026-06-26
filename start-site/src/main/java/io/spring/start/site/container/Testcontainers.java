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

/**
 * Manages testcontainers with their container names and modules.
 *
 * @author Moritz Halbritter
 * @author Eddú Meléndez
 */
public class Testcontainers {

	/**
	 * Class name of the generic container.
	 */
	public static final String GENERIC_CONTAINER_CLASS_NAME = "org.testcontainers.containers.GenericContainer";

	/**
	 * Returns the container details for the given supported container.
	 * @param supportedContainer the supported container
	 * @return the container details
	 */
	public Container getContainer(SupportedContainer supportedContainer) {
		return switch (supportedContainer) {
			case ACTIVEMQ ->
				Container.of("org.testcontainers.activemq.ActiveMQContainer", false, "testcontainers-activemq");
			case ARTEMIS ->
				Container.of("org.testcontainers.activemq.ArtemisContainer", false, "testcontainers-activemq");
			case CASSANDRA ->
				Container.of("org.testcontainers.cassandra.CassandraContainer", false, "testcontainers-cassandra");
			case CHROMADB ->
				Container.of("org.testcontainers.chromadb.ChromaDBContainer", false, "testcontainers-chromadb");
			case ELASTICSEARCH -> Container.of("org.testcontainers.elasticsearch.ElasticsearchContainer", false,
					"testcontainers-elasticsearch");
			case GENERIC -> Container.of(GENERIC_CONTAINER_CLASS_NAME, true, null);
			case GRAFANA_LGTM ->
				Container.of("org.testcontainers.grafana.LgtmStackContainer", false, "testcontainers-grafana");
			case KAFKA -> Container.of("org.testcontainers.kafka.KafkaContainer", false, "testcontainers-kafka");
			case MARIADB ->
				Container.of("org.testcontainers.mariadb.MariaDBContainer", false, "testcontainers-mariadb");
			case MILVUS -> Container.of("org.testcontainers.milvus.MilvusContainer", false, "testcontainers-milvus");
			case MONGODB ->
				Container.of("org.testcontainers.mongodb.MongoDBContainer", false, "testcontainers-mongodb");
			case MONGODB_ATLAS ->
				Container.of("org.testcontainers.mongodb.MongoDBAtlasLocalContainer", false, "testcontainers-mongodb");
			case MSSQL -> Container.of("org.testcontainers.mssqlserver.MSSQLServerContainer", false,
					"testcontainers-mssqlserver");
			case MYSQL -> Container.of("org.testcontainers.mysql.MySQLContainer", false, "testcontainers-mysql");
			case NEO4J -> Container.of("org.testcontainers.neo4j.Neo4jContainer", false, "testcontainers-neo4j");
			case OLLAMA -> Container.of("org.testcontainers.ollama.OllamaContainer", false, "testcontainers-ollama");
			case ORACLE ->
				Container.of("org.testcontainers.oracle.OracleContainer", false, "testcontainers-oracle-free");
			case POSTGRESQL ->
				Container.of("org.testcontainers.postgresql.PostgreSQLContainer", false, "testcontainers-postgresql");
			case PULSAR -> Container.of("org.testcontainers.pulsar.PulsarContainer", false, "testcontainers-pulsar");
			case QDRANT -> Container.of("org.testcontainers.qdrant.QdrantContainer", false, "testcontainers-qdrant");
			case RABBITMQ ->
				Container.of("org.testcontainers.rabbitmq.RabbitMQContainer", false, "testcontainers-rabbitmq");
			case TYPESENSE ->
				Container.of("org.testcontainers.typesense.TypesenseContainer", false, "testcontainers-typesense");
			case WEAVIATE ->
				Container.of("org.testcontainers.weaviate.WeaviateContainer", false, "testcontainers-weaviate");
		};
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
		 * Typesense.
		 */
		TYPESENSE,
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
