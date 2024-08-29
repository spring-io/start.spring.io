/*
 * Copyright 2012-2024 the original author or authors.
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

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link SimpleDockerServiceResolver}.
 *
 * @author Fer Clager
 */
@SpringBootTest
public class SimpleDockerServiceResolverTests {

	private final SimpleDockerServiceResolver resolver;

	@Autowired
	public SimpleDockerServiceResolverTests(SimpleDockerServiceResolver resolver) {
		this.resolver = resolver;
	}

	@ParameterizedTest
	@MethodSource("provideData")
	void resolveService_whenValidId(String id, String image, String url, int numPorts) {
		DockerService resolved = this.resolver.resolve(id);
		assertInstanceOf(DockerService.class, resolved);
		assertEquals(image, resolved.getImage());
		assertEquals(url, resolved.getWebsite());
		assertNotNull(resolved.getPorts());
		assertEquals(numPorts, resolved.getPorts().length);
	}

	@Test
	void notResolveService_whenInvalidId() {
		DockerService resolved = this.resolver.resolve("invalid-id");
		assertNull(resolved);
	}

	private static Stream<Arguments> provideData() {
		return Stream.of(Arguments.of("activeMQ", "symptoma/activemq", "https://hub.docker.com/r/symptoma/activemq", 1),
				Arguments.of("activeMQClassic", "apache/activemq-classic",
						"https://hub.docker.com/r/apache/activemq-classic", 1),
				Arguments
					.of("artemis", "apache/activemq-artemis", "https://hub.docker.com/r/apache/activemq-artemis", 1),
				Arguments.of("cassandra", "cassandra", "https://hub.docker.com/_/cassandra", 1),
				Arguments.of("elasticsearch", "docker.elastic.co/elasticsearch/elasticsearch",
						"https://www.docker.elastic.co/r/elasticsearch", 2),
				Arguments.of("kafka", "confluentinc/cp-kafka", "https://hub.docker.com/r/confluentinc/cp-kafka", 1),
				Arguments.of("mariaDb", "mariadb", "https://hub.docker.com/_/mariadb", 1),
				Arguments.of("mongoDb", "mongo", "https://hub.docker.com/_/mongo", 1),
				Arguments.of("mysql", "mysql", "https://hub.docker.com/_/mysql", 1),
				Arguments.of("neo4j", "neo4j", "https://hub.docker.com/_/neo4j", 1),
				Arguments.of("oracleFree", "gvenzl/oracle-free", "https://hub.docker.com/r/gvenzl/oracle-free", 1),
				Arguments.of("pgvector", "pgvector/pgvector", "https://hub.docker.com/r/pgvector/pgvector", 1),
				Arguments.of("postgres", "postgres", "https://hub.docker.com/_/postgres", 1),
				Arguments.of("pulsar", "apachepulsar/pulsar", "https://hub.docker.com/r/apachepulsar/pulsar", 2),
				Arguments.of("rabbit", "rabbitmq", "https://hub.docker.com/_/rabbitmq", 1),
				Arguments.of("redis", "redis", "https://hub.docker.com/_/redis", 1),
				Arguments.of("sqlServer", "mcr.microsoft.com/mssql/server",
						"https://mcr.microsoft.com/en-us/product/mssql/server/about/", 1),
				Arguments.of("zipkin", "openzipkin/zipkin", "https://hub.docker.com/r/openzipkin/zipkin/", 1));
	}

}
