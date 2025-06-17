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

package io.spring.start.site.extension.dependency.flyway;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FlywayBuildCustomizer}.
 *
 * @author Eddú Meléndez
 * @author Moritz Halbritter
 */
class FlywayBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void mariadbOnly() {
		ProjectRequest projectRequest = createProject("mariadb");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("mariadb"))
			.doesNotHaveDependency("org.flywaydb", "flyway-mysql");
	}

	@Test
	void mysqlOnly() {
		ProjectRequest projectRequest = createProject("mysql");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("mysql"))
			.doesNotHaveDependency("org.flywaydb", "flyway-mysql");
	}

	@Test
	void sqlserverOnly() {
		ProjectRequest projectRequest = createProject("sqlserver");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("sqlserver"))
			.doesNotHaveDependency("org.flywaydb", "flyway-sqlserver");
	}

	@Test
	void oracleOnly() {
		ProjectRequest projectRequest = createProject("oracle");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("oracle"))
			.doesNotHaveDependency("org.flywaydb", "flyway-database-oracle");
	}

	@Test
	void mariadbAndFlyway() {
		ProjectRequest projectRequest = createProject("mariadb", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("mariadb"))
			.hasDependency("org.flywaydb", "flyway-mysql");
	}

	@Test
	void mysqlAndFlyway() {
		ProjectRequest projectRequest = createProject("mysql", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("mysql"))
			.hasDependency("org.flywaydb", "flyway-mysql");
	}

	@Test
	void sqlserverAndFlyway() {
		ProjectRequest projectRequest = createProject("sqlserver", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("sqlserver"))
			.hasDependency("org.flywaydb", "flyway-sqlserver");
	}

	@Test
	void oracleAndFlyway() {
		ProjectRequest projectRequest = createProject("oracle", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("oracle"))
			.hasDependency("org.flywaydb", "flyway-database-oracle");
	}

	@Test
	void db2AndFlyway() {
		ProjectRequest projectRequest = createProject("db2", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("db2"))
			.hasDependency("org.flywaydb", "flyway-database-db2");
	}

	@Test
	void derbyAndFlyway() {
		ProjectRequest projectRequest = createProject("derby", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("derby"))
			.hasDependency("org.flywaydb", "flyway-database-derby");
	}

	@Test
	void hsqlAndFlyway() {
		ProjectRequest projectRequest = createProject("hsql", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("hsql"))
			.hasDependency("org.flywaydb", "flyway-database-hsqldb");
	}

	@Test
	void hsqlAndPostgres() {
		ProjectRequest projectRequest = createProject("postgresql", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("postgresql"))
			.hasDependency("org.flywaydb", "flyway-database-postgresql");
	}

	private ProjectRequest createProject(String... styles) {
		ProjectRequest projectRequest = createProjectRequest(styles);
		projectRequest.setLanguage("java");
		return projectRequest;
	}

}
