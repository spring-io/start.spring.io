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

package io.spring.start.site.extension.description;

import java.util.Objects;
import java.util.function.Consumer;

import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionDiff;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that adds a warning when the JVM level was changed.
 *
 * @author Stephane Nicoll
 */
public class InvalidJvmVersionHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final ProjectDescriptionDiff diff;

	private final ProjectDescription description;

	public InvalidJvmVersionHelpDocumentCustomizer(ProjectDescriptionDiff diff, ProjectDescription description) {
		this.diff = diff;
		this.description = description;
	}

	@Override
	public void customize(HelpDocument document) {
		this.diff.ifLanguageChanged(this.description, (original, current) -> {
			String originalJvmVersion = original.jvmVersion();
			String actualJvmVersion = current.jvmVersion();
			if (!Objects.equals(originalJvmVersion, actualJvmVersion)) {
				processJvmVersionDiff(originalJvmVersion, actualJvmVersion).accept(document);
			}
		});
	}

	protected Consumer<HelpDocument> processJvmVersionDiff(String originalJvmVersion, String actualJvmVersion) {
		return (document) -> {
			if (this.description.getLanguage() instanceof KotlinLanguage) {
				document.getWarnings()
					.addItem(
							"The JVM level was changed from '%s' to '%s' as the Kotlin version does not support Java %s yet."
								.formatted(originalJvmVersion, actualJvmVersion, originalJvmVersion));
			}
			else {
				document.getWarnings()
					.addItem(
							"The JVM level was changed from '%s' to '%s', review the [JDK Version Range](%s) on the wiki for more details."
								.formatted(originalJvmVersion, actualJvmVersion,
										"https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-Versions#jdk-version-range"));
			}
		};

	}

}
