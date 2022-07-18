/*
 * Copyright 2012-2022 the original author or authors.
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
 */
class FlywayBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void mariadbOnly() {
		ProjectRequest projectRequest = createProject("2.7.0", "mariadb");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("mariadb"))
				.doesNotHaveDependency("org.flywaydb", "flyway-mysql");
	}

	@Test
	void mysqlOnly() {
		ProjectRequest projectRequest = createProject("2.7.0", "mysql");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("mysql")).doesNotHaveDependency("org.flywaydb",
				"flyway-mysql");
	}

	@Test
	void sqlserverOnly() {
		ProjectRequest projectRequest = createProject("2.7.0", "sqlserver");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("sqlserver"))
				.doesNotHaveDependency("org.flywaydb", "flyway-sqlserver");
	}

	@Test
	void mariadbAndFlywayPreviousTo270() {
		ProjectRequest projectRequest = createProject("2.6.0", "mariadb", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("mariadb"))
				.doesNotHaveDependency("org.flywaydb", "flyway-mysql");
	}

	@Test
	void mysqlAndFlywayPreviousTo270() {
		ProjectRequest projectRequest = createProject("2.6.0", "mysql", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("mysql")).doesNotHaveDependency("org.flywaydb",
				"flyway-mysql");
	}

	@Test
	void sqlserverAndFlywayPreviousTo270() {
		ProjectRequest projectRequest = createProject("2.6.0", "sqlserver", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("sqlserver"))
				.doesNotHaveDependency("org.flywaydb", "flyway-sqlserver");
	}

	@Test
	void mariadbAndFlyway() {
		ProjectRequest projectRequest = createProject("2.7.0", "mariadb", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("mariadb")).hasDependency("org.flywaydb",
				"flyway-mysql");
	}

	@Test
	void mysqlAndFlyway() {
		ProjectRequest projectRequest = createProject("2.7.0", "mysql", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("mysql")).hasDependency("org.flywaydb",
				"flyway-mysql");
	}

	@Test
	void sqlserverAndFlyway() {
		ProjectRequest projectRequest = createProject("2.7.0", "sqlserver", "flyway");
		assertThat(mavenPom(projectRequest)).hasDependency(getDependency("sqlserver")).hasDependency("org.flywaydb",
				"flyway-sqlserver");
	}

	private ProjectRequest createProject(String springBootVersion, String... styles) {
		ProjectRequest projectRequest = createProjectRequest(styles);
		projectRequest.setLanguage("java");
		projectRequest.setJavaVersion("11");
		projectRequest.setBootVersion(springBootVersion);
		return projectRequest;
	}

}
