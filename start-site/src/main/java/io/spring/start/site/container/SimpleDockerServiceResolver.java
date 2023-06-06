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

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link DockerServiceResolver} implementation that hard-codes well known docker
 * services.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
public class SimpleDockerServiceResolver implements DockerServiceResolver {

	private final Map<String, DockerService> dockerServices;

	public SimpleDockerServiceResolver() {
		this.dockerServices = new HashMap<>();
		this.dockerServices.put("elasticsearch", elasticsearch());
		this.dockerServices.put("cassandra", cassandra());
		this.dockerServices.put("mariaDb", mariaDb());
		this.dockerServices.put("mongoDb", mongoDb());
		this.dockerServices.put("mysql", mysql());
		this.dockerServices.put("oracle", oracle());
		this.dockerServices.put("postgres", postgres());
		this.dockerServices.put("rabbit", rabbit());
		this.dockerServices.put("redis", redis());
		this.dockerServices.put("sqlServer", sqlServer());
		this.dockerServices.put("zipkin", zipkin());
	}

	private static DockerService elasticsearch() {
		// They don't provide a 'latest' tag
		return new DockerService("docker.elastic.co/elasticsearch/elasticsearch", "8.7.1",
				"https://www.docker.elastic.co/r/elasticsearch", 9200, 9300);
	}

	private static DockerService cassandra() {
		return new DockerService("cassandra", "latest", "https://hub.docker.com/_/cassandra", 9042);
	}

	private static DockerService mariaDb() {
		return new DockerService("mariadb", "latest", "https://hub.docker.com/_/mariadb", 3306);
	}

	private static DockerService mongoDb() {
		return new DockerService("mongo", "latest", "https://hub.docker.com/_/mongo", 27017);
	}

	private static DockerService mysql() {
		return new DockerService("mysql", "latest", "https://hub.docker.com/_/mysql", 3306);
	}

	private static DockerService oracle() {
		return new DockerService("gvenzl/oracle-xe", "latest", "https://hub.docker.com/r/gvenzl/oracle-xe", 1521);
	}

	private static DockerService postgres() {
		return new DockerService("postgres", "latest", "https://hub.docker.com/_/postgres", 5432);
	}

	private static DockerService rabbit() {
		return new DockerService("rabbitmq", "latest", "https://hub.docker.com/_/rabbitmq", 5672);
	}

	private static DockerService redis() {
		return new DockerService("redis", "latest", "https://hub.docker.com/_/redis", 6379);
	}

	private static DockerService sqlServer() {
		return new DockerService("mcr.microsoft.com/mssql/server", "latest",
				"https://mcr.microsoft.com/en-us/product/mssql/server/about/", 1433);
	}

	private static DockerService zipkin() {
		return new DockerService("openzipkin/zipkin", "latest", "https://hub.docker.com/r/openzipkin/zipkin/", 9411);
	}

	@Override
	public DockerService resolve(String id) {
		return this.dockerServices.get(id);
	}

}
