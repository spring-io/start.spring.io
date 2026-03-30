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

package io.spring.start.site.extension.dependency.sqlite;

import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for SQLite project generation.
 *
 * @author Sahil Sobhani
 */
class SqliteProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void whenSqliteIsSelectedAddsSqliteJdbc() {
		assertThat(mavenPom(createProjectRequest("sqlite"))).hasDependency("org.xerial", "sqlite-jdbc", null,
				"runtime");
	}

	@Test
	void whenSqliteIsNotSelectedDoesNotAddSqliteJdbc() {
		assertThat(mavenPom(createProjectRequest("web"))).doesNotHaveDependency("org.xerial", "sqlite-jdbc");
	}

}
