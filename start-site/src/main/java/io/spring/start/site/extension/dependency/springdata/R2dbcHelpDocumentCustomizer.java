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

import java.util.Arrays;
import java.util.List;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * A {@link HelpDocumentCustomizer} that adds a section when R2DBC is selected but no
 * driver was selected.
 *
 * @author Stephane Nicoll
 */
public class R2dbcHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final List<String> DRIVERS = Arrays.asList("h2", "mysql", "mariadb", "postgresql", "sqlserver",
			"oracle");

	private static final VersionRange SPRING_BOOT_3_1_0_OR_LATER = VersionParser.DEFAULT.parseRange("3.1.0");

	private final boolean mysqlR2dbcIsAsyncerDependency;

	private final Build build;

	public R2dbcHelpDocumentCustomizer(Build build, Version platformVersion) {
		this.build = build;
		this.mysqlR2dbcIsAsyncerDependency = SPRING_BOOT_3_1_0_OR_LATER.match(platformVersion);
	}

	@Override
	public void customize(HelpDocument document) {
		if (this.build.dependencies().ids().noneMatch(DRIVERS::contains) || !this.mysqlR2dbcIsAsyncerDependency) {
			document.addSection((writer) -> {
				writer.println("## Missing R2DBC Driver");
				writer.println();
				writer.println(
						"Make sure to include a [R2DBC Driver](https://r2dbc.io/drivers/) to connect to your database.");
			});
		}
	}

}
