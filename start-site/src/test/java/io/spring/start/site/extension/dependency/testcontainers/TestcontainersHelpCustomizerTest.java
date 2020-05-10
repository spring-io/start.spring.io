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

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link TestcontainersHelpCustomizer}.
 *
 * @author Maciej Walkowiak
 */
class TestcontainersHelpCustomizerTest {

	@ParameterizedTest
	@ValueSource(strings = { "postgresql", "mysql", "sqlserver", "oracle" })
	void jdbcWithNoMatchingDriver(String driver) {
		HelpDocument helpDocument = createHelpDocument("testcontainers", driver);
		assertThat(helpDocument.gettingStarted().referenceDocs().getItems()).hasSize(1);
		assertThat(helpDocument.gettingStarted().referenceDocs().getItems().get(0).getHref())
				.isEqualTo("https://www.testcontainers.org/modules/databases/jdbc/");
	}

	@ParameterizedTest
	@ValueSource(strings = { "postgresql", "mysql", "sqlserver", "oracle" })
	void r2dbcWithNoMatchingDriver(String driver) {
		HelpDocument helpDocument = createHelpDocument("testcontainers", "data-r2dbc", driver);
		assertThat(helpDocument.gettingStarted().referenceDocs().getItems()).hasSize(1);
		assertThat(helpDocument.gettingStarted().referenceDocs().getItems().get(0).getHref())
				.isEqualTo("https://www.testcontainers.org/modules/databases/r2dbc/");
	}

	private HelpDocument createHelpDocument(String... dependencyIds) {
		Build build = new GradleBuild();
		for (String dependencyId : dependencyIds) {
			build.dependencies().add(dependencyId, Dependency.withCoordinates(dependencyId, dependencyId));
		}
		HelpDocument document = new HelpDocument(mock(MustacheTemplateRenderer.class));
		new TestcontainersHelpCustomizer(build).customize(document);
		return document;
	}

}
