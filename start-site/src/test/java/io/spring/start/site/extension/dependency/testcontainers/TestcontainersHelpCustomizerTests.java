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

import java.util.stream.Stream;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TestcontainersHelpCustomizer}.
 *
 * @author Maciej Walkowiak
 * @author Stephane Nicoll
 */
class TestcontainersHelpCustomizerTests extends AbstractExtensionTests {

	@ParameterizedTest
	@MethodSource("supportedEntries")
	void linkToSupportedEntriesWhenTestContainerIsPresentIsAdded(String dependencyId, String docHref) {
		assertHelpDocument("testcontainers", dependencyId)
				.contains("https://www.testcontainers.org/modules/" + docHref);
	}

	@ParameterizedTest
	@MethodSource("supportedEntries")
	void linkToSupportedEntriesWhenTestContainerIsNotPresentIsNotAdded(String dependencyId, String docHref) {
		assertHelpDocument(dependencyId).doesNotContain("https://www.testcontainers.org/modules/" + docHref);
	}

	@Test
	void linkToSupportedEntriesWhenTwoMatchesArePresentOnlyAddLinkOnce() {
		assertHelpDocument("testcontainers", "data-mongodb", "data-mongodb-reactive")
				.containsOnlyOnce("https://www.testcontainers.org/modules/databases/mongodb/");
	}

	static Stream<Arguments> supportedEntries() {
		return Stream.of(Arguments.arguments("amqp", "rabbitmq/"),
				Arguments.arguments("data-cassandra", "databases/cassandra/"),
				Arguments.arguments("data-cassandra-reactive", "databases/cassandra/"),
				Arguments.arguments("data-couchbase", "databases/couchbase/"),
				Arguments.arguments("data-couchbase-reactive", "databases/couchbase/"),
				Arguments.arguments("data-elasticsearch", "elasticsearch/"),
				Arguments.arguments("data-mongodb", "databases/mongodb/"),
				Arguments.arguments("data-mongodb-reactive", "databases/mongodb/"),
				Arguments.arguments("data-neo4j", "databases/neo4j/"),
				Arguments.arguments("data-r2dbc", "databases/r2dbc/"), Arguments.arguments("data-solr", "solr/"),
				Arguments.arguments("db2", "databases/db2"), Arguments.arguments("kafka", "kafka/"),
				Arguments.arguments("mariadb", "databases/mariadb/"), Arguments.arguments("kafka-streams", "kafka/"),
				Arguments.arguments("mysql", "databases/mysql/"),
				Arguments.arguments("postgresql", "databases/postgres/"),
				Arguments.arguments("oracle", "databases/oraclexe/"),
				Arguments.arguments("sqlserver", "databases/mssqlserver/"));
	}

	private TextAssert assertHelpDocument(String... dependencyIds) {
		ProjectRequest request = createProjectRequest(dependencyIds);
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md"));
	}

}
