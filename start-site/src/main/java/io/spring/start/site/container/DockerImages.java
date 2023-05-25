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

package io.spring.start.site.container;

/**
 * Provides the names of common docker images.
 *
 * @author Moritz Halbritter
 */
public final class DockerImages {

	private DockerImages() {
		// Static class
	}

	/**
	 * Elasticsearch.
	 * @return the Elasticsearch image
	 */
	public static DockerImage elasticsearch() {
		// They don't provide a 'latest' tag
		return new DockerImage("docker.elastic.co/elasticsearch/elasticsearch", "8.7.1",
				"https://www.docker.elastic.co/r/elasticsearch");
	}

	/**
	 * Cassandra.
	 * @return the Cassandra image
	 */
	public static DockerImage cassandra() {
		return new DockerImage("cassandra", "latest", "https://hub.docker.com/_/cassandra");
	}

	/**
	 * MariaDB.
	 * @return the MariaDB image
	 */
	public static DockerImage mariaDb() {
		return new DockerImage("mariadb", "latest", "https://hub.docker.com/_/mariadb");
	}

	/**
	 * MongoDB.
	 * @return the MongoDB image
	 */
	public static DockerImage mongoDb() {
		return new DockerImage("mongo", "latest", "https://hub.docker.com/_/mongo");
	}

	/**
	 * MySQL.
	 * @return the MySQL image
	 */
	public static DockerImage mysql() {
		return new DockerImage("mysql", "latest", "https://hub.docker.com/_/mysql");
	}

	/**
	 * Oracle.
	 * @return the Oracle image
	 */
	public static DockerImage oracle() {
		return new DockerImage("gvenzl/oracle-xe", "latest", "https://hub.docker.com/r/gvenzl/oracle-xe");
	}

	/**
	 * PostgreSQL.
	 * @return the PostgreSQL image
	 */
	public static DockerImage postgres() {
		return new DockerImage("postgres", "latest", "https://hub.docker.com/_/postgres");
	}

	/**
	 * RabbitMQ.
	 * @return the RabbitMQ image
	 */
	public static DockerImage rabbit() {
		return new DockerImage("rabbitmq", "latest", "https://hub.docker.com/_/rabbitmq");
	}

	/**
	 * Redis.
	 * @return the Redis image
	 */
	public static DockerImage redis() {
		return new DockerImage("redis", "latest", "https://hub.docker.com/_/redis");
	}

	/**
	 * SQL server.
	 * @return the SQL server image
	 */
	public static DockerImage sqlServer() {
		return new DockerImage("mcr.microsoft.com/mssql/server", "latest",
				"https://mcr.microsoft.com/en-us/product/mssql/server/about/");
	}

	/**
	 * Zipkin.
	 * @return the Zipkin image
	 */
	public static DockerImage zipkin() {
		return new DockerImage("openzipkin/zipkin", "latest", "https://hub.docker.com/r/openzipkin/zipkin/");
	}

	/**
	 * A Docker image.
	 *
	 * @param image the name of the image
	 * @param tag the tag of the image
	 * @param website the website of the image
	 */
	public record DockerImage(String image, String tag, String website) {
	}

}
