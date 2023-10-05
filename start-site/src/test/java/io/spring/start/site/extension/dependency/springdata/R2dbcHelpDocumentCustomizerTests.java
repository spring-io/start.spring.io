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

package io.spring.start.site.extension.dependency.springdata;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.version.Version;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link R2dbcHelpDocumentCustomizer}.
 *
 * @author Stephane Nicoll
 */
class R2dbcHelpDocumentCustomizerTests {

	@Test
	void r2dbcWithNoMatchingDriver() {
		HelpDocument helpDocument = createHelpDocument(Version.parse("3.1.0"));
		assertThat(helpDocument.getSections()).hasSize(1);
	}

	@Test
	void r2dbcWithH2() {
		HelpDocument helpDocument = createHelpDocument(Version.parse("3.1.0"), "h2");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithMariadb() {
		HelpDocument helpDocument = createHelpDocument(Version.parse("3.1.0"), "mariadb");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithPostgresql() {
		HelpDocument helpDocument = createHelpDocument(Version.parse("3.1.0"), "postgresql");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithSqlServer() {
		HelpDocument helpDocument = createHelpDocument(Version.parse("3.1.0"), "sqlserver");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithOracle() {
		HelpDocument helpDocument = createHelpDocument(Version.parse("3.1.0"), "oracle");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithMysql() {
		HelpDocument helpDocument = createHelpDocument(Version.parse("3.1.0"), "mysql");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithMysqlBeforeVersion() {
		HelpDocument helpDocument = createHelpDocument(Version.parse("2.7.0.M1"), "mysql");
		assertThat(helpDocument.getSections()).hasSize(1);
	}

	@Test
	void r2dbcWithSeveralDrivers() {
		HelpDocument helpDocument = createHelpDocument(Version.parse("3.1.0"), "mysql", "h2");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	private HelpDocument createHelpDocument(Version platformVersion, String... dependencyIds) {
		Build build = new GradleBuild();
		for (String dependencyId : dependencyIds) {
			build.dependencies().add(dependencyId, Dependency.withCoordinates(dependencyId, dependencyId));
		}
		HelpDocument document = new HelpDocument(mock(MustacheTemplateRenderer.class));
		new R2dbcHelpDocumentCustomizer(build, platformVersion).customize(document);
		return document;
	}

}
