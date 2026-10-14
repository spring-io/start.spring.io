/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springdata;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
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
		HelpDocument helpDocument = createHelpDocument();
		assertThat(helpDocument.getSections()).hasSize(1);
	}

	@Test
	void r2dbcWithH2() {
		HelpDocument helpDocument = createHelpDocument("h2");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithMariadb() {
		HelpDocument helpDocument = createHelpDocument("mariadb");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithPostgresql() {
		HelpDocument helpDocument = createHelpDocument("postgresql");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithSqlServer() {
		HelpDocument helpDocument = createHelpDocument("sqlserver");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithOracle() {
		HelpDocument helpDocument = createHelpDocument("oracle");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithMysql() {
		HelpDocument helpDocument = createHelpDocument("mysql");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	@Test
	void r2dbcWithSeveralDrivers() {
		HelpDocument helpDocument = createHelpDocument("mysql", "h2");
		assertThat(helpDocument.getSections()).isEmpty();
	}

	private HelpDocument createHelpDocument(String... dependencyIds) {
		Build build = new GradleBuild();
		for (String dependencyId : dependencyIds) {
			build.dependencies().add(dependencyId, Dependency.withCoordinates(dependencyId, dependencyId));
		}
		HelpDocument document = new HelpDocument(mock(MustacheTemplateRenderer.class));
		new R2dbcHelpDocumentCustomizer(build).customize(document);
		return document;
	}

}
