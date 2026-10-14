/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springdata;

import java.util.List;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that adds a section when R2DBC is selected but no
 * driver was selected.
 *
 * @author Stephane Nicoll
 */
public class R2dbcHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final List<String> DRIVERS = List.of("h2", "mariadb", "mysql", "postgresql", "sqlserver", "oracle");

	private final Build build;

	public R2dbcHelpDocumentCustomizer(Build build) {
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		if (this.build.dependencies().ids().noneMatch(DRIVERS::contains)) {
			document.addSection((writer) -> {
				writer.println("## Missing R2DBC Driver");
				writer.println();
				writer.println(
						"Make sure to include a [R2DBC Driver](https://r2dbc.io/drivers/) to connect to your database.");
			});
		}
	}

}
