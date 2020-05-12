/*
 * Copyright 2012-2019 the original author or authors.
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

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that adds a reference links when Testcontainers and
 * one of supported NoSQL databases, JDBC or R2DBC drivers is selected.
 *
 * @author Maciej Walkowiak
 */
public class TestcontainersHelpCustomizer implements HelpDocumentCustomizer {

	private static final List<String> DRIVERS = Arrays.asList("mysql", "postgresql", "sqlserver", "oracle");

	private final Build build;

	public TestcontainersHelpCustomizer(Build build) {
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		if (this.build.dependencies().ids().anyMatch(DRIVERS::contains)) {
			if (this.build.dependencies().has("data-r2dbc")) {
				document.gettingStarted().addReferenceDocLink("https://www.testcontainers.org/modules/databases/r2dbc/",
						"Testcontainers R2DBC support reference");
			}
			else {
				document.gettingStarted().addReferenceDocLink("https://www.testcontainers.org/modules/databases/jdbc/",
						"Testcontainers JDBC support reference");
			}
		}

		if (this.build.dependencies().has("data-neo4j")) {
			document.gettingStarted().addReferenceDocLink("https://www.testcontainers.org/modules/databases/neo4j/",
					"Testcontainers Neo4j support reference");
		}

		if (this.build.dependencies().has("data-cassandra")
				|| this.build.dependencies().has("data-cassandra-reactive")) {
			document.gettingStarted().addReferenceDocLink("https://www.testcontainers.org/modules/databases/cassandra/",
					"Testcontainers Cassandra support reference");
		}

		if (this.build.dependencies().has("data-couchbase")
				|| this.build.dependencies().has("data-couchbase-reactive")) {
			document.gettingStarted().addReferenceDocLink("https://www.testcontainers.org/modules/databases/couchbase/",
					"Testcontainers Couchbase support reference");
		}
	}

}
