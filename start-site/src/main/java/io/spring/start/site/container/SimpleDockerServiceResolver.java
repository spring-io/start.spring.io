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

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link DockerServiceResolver} implementation that hard-codes well known docker
 * services.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 * @author Chris Bono
 * @author Eddú Meléndez
 */
public class SimpleDockerServiceResolver implements DockerServiceResolver {

	private final Map<String, DockerService> dockerServices;

	public SimpleDockerServiceResolver() {
		this.dockerServices = new HashMap<>();
		this.dockerServices.put("activeMQ", activeMQ());
		this.dockerServices.put("activeMQClassic", activeMQClassic());
		this.dockerServices.put("artemis", artemis());
		this.dockerServices.put("azurite", azurite());
		this.dockerServices.put("cassandra", cassandra());
		this.dockerServices.put("chroma", chroma());
		this.dockerServices.put("elasticsearch", elasticsearch());
		this.dockerServices.put("kafka", kafka());
		this.dockerServices.put("kafka-native", kafkaNative());
		this.dockerServices.put("mariaDb", mariaDb());
		this.dockerServices.put("milvus", milvus());
		this.dockerServices.put("mongoDb", mongoDb());
		this.dockerServices.put("mongoDbAtlas", mongoDbAtlas());
		this.dockerServices.put("mysql", mysql());
		this.dockerServices.put("neo4j", neo4j());
		this.dockerServices.put("ollama", ollama());
		this.dockerServices.put("oracleFree", oracleFree());
		this.dockerServices.put("pgvector", pgvector());
		this.dockerServices.put("postgres", postgres());
		this.dockerServices.put("pulsar", pulsar());
		this.dockerServices.put("qdrant", qdrant());
		this.dockerServices.put("rabbit", rabbit());
		this.dockerServices.put("redis", redis());
		this.dockerServices.put("redisStack", redisStack());
		this.dockerServices.put("sqlServer", sqlServer());
		this.dockerServices.put("weaviate", weaviate());
		this.dockerServices.put("zipkin", zipkin());
	}

	private static DockerService activeMQ() {
		return DockerService.withImageAndTag("symptoma/activemq")
			.website("https://hub.docker.com/r/symptoma/activemq")
			.ports(61616)
			.build();
	}

	private static DockerService activeMQClassic() {
		return DockerService.withImageAndTag("apache/activemq-classic")
			.website("https://hub.docker.com/r/apache/activemq-classic")
			.ports(61616)
			.build();
	}

	private static DockerService artemis() {
		return DockerService.withImageAndTag("apache/activemq-artemis")
			.website("https://hub.docker.com/r/apache/activemq-artemis")
			.ports(61616)
			.build();
	}

	private static DockerService azurite() {
		return DockerService.withImageAndTag("mcr.microsoft.com/azure-storage/azurite")
			.website("https://github.com/Azure/Azurite?tab=readme-ov-file#dockerhub")
			.ports(10000, 10001, 10002)
			.build();
	}

	private static DockerService cassandra() {
		return DockerService.withImageAndTag("cassandra")
			.website("https://hub.docker.com/_/cassandra")
			.ports(9042)
			.build();
	}

	private static DockerService chroma() {
		return DockerService.withImageAndTag("chromadb/chroma")
			.website("https://hub.docker.com/r/chromadb/chroma")
			.ports(8000)
			.build();
	}

	private static DockerService elasticsearch() {
		// They don't provide a 'latest' tag
		return DockerService.withImageAndTag("docker.elastic.co/elasticsearch/elasticsearch:7.17.10")
			.website("https://www.docker.elastic.co/r/elasticsearch")
			.ports(9200, 9300)
			.build();
	}

	private static DockerService kafka() {
		return DockerService.withImageAndTag("confluentinc/cp-kafka")
			.website("https://hub.docker.com/r/confluentinc/cp-kafka")
			.ports(9092)
			.build();
	}

	private static DockerService kafkaNative() {
		return DockerService.withImageAndTag("apache/kafka-native")
			.website("https://hub.docker.com/r/apache/kafka-native")
			.ports(9092)
			.build();
	}

	private static DockerService mariaDb() {
		return DockerService.withImageAndTag("mariadb").website("https://hub.docker.com/_/mariadb").ports(3306).build();
	}

	private static DockerService milvus() {
		return DockerService.withImageAndTag("milvusdb/milvus")
			.website("https://hub.docker.com/r/milvusdb/milvus")
			.ports(19530)
			.build();
	}

	private static DockerService mongoDb() {
		return DockerService.withImageAndTag("mongo").website("https://hub.docker.com/_/mongo").ports(27017).build();
	}

	private static DockerService mongoDbAtlas() {
		return DockerService.withImageAndTag("mongodb/mongodb-atlas-local")
			.website("https://hub.docker.com/r/mongodb/mongodb-atlas-local")
			.ports(27017)
			.build();
	}

	private static DockerService mysql() {
		return DockerService.withImageAndTag("mysql").website("https://hub.docker.com/_/mysql").ports(3306).build();
	}

	private static DockerService neo4j() {
		return DockerService.withImageAndTag("neo4j").website("https://hub.docker.com/_/neo4j").ports(7687).build();
	}

	private static DockerService ollama() {
		return DockerService.withImageAndTag("ollama/ollama")
			.website("https://hub.docker.com/r/ollama/ollama")
			.ports(11434)
			.build();
	}

	private static DockerService oracleFree() {
		return DockerService.withImageAndTag("gvenzl/oracle-free")
			.website("https://hub.docker.com/r/gvenzl/oracle-free")
			.ports(1521)
			.build();
	}

	private static DockerService pgvector() {
		return DockerService.withImageAndTag("pgvector/pgvector:pg16")
			.website("https://hub.docker.com/r/pgvector/pgvector")
			.ports(5432)
			.build();
	}

	private static DockerService postgres() {
		return DockerService.withImageAndTag("postgres")
			.website("https://hub.docker.com/_/postgres")
			.ports(5432)
			.build();
	}

	private static DockerService pulsar() {
		return DockerService.withImageAndTag("apachepulsar/pulsar")
			.website("https://hub.docker.com/r/apachepulsar/pulsar")
			.command("bin/pulsar standalone")
			.ports(8080, 6650)
			.build();
	}

	private static DockerService qdrant() {
		return DockerService.withImageAndTag("qdrant/qdrant")
			.website("https://hub.docker.com/r/qdrant/qdrant")
			.ports(6334)
			.build();
	}

	private static DockerService rabbit() {
		return DockerService.withImageAndTag("rabbitmq")
			.website("https://hub.docker.com/_/rabbitmq")
			.ports(5672)
			.build();
	}

	private static DockerService redis() {
		return DockerService.withImageAndTag("redis").website("https://hub.docker.com/_/redis").ports(6379).build();
	}

	private static DockerService redisStack() {
		return DockerService.withImageAndTag("redis/redis-stack")
			.website("https://hub.docker.com/r/redis/redis-stack")
			.ports(6379)
			.build();
	}

	private static DockerService sqlServer() {
		return DockerService.withImageAndTag("mcr.microsoft.com/mssql/server")
			.website("https://mcr.microsoft.com/en-us/product/mssql/server/about/")
			.ports(1433)
			.build();
	}

	private static DockerService weaviate() {
		return DockerService.withImageAndTag("semitechnologies/weaviate")
			.website("https://hub.docker.com/r/semitechnologies/weaviate")
			.ports(8080)
			.build();
	}

	private static DockerService zipkin() {
		return DockerService.withImageAndTag("openzipkin/zipkin")
			.website("https://hub.docker.com/r/openzipkin/zipkin/")
			.ports(9411)
			.build();
	}

	@Override
	public DockerService resolve(String id) {
		return this.dockerServices.get(id);
	}

}
