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

package io.spring.start.site.extension.dependency.timefold;

import java.util.Set;

import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.project.ProjectDescriptionField;
import io.spring.start.site.project.JavaVersionProjectDescriptionCustomizer;

/**
 * Validate that the requested java version is compatible with the chosen Spring Boot
 * generation and adapt the request if necessary.
 * <p>
 * Timefold 2.1.0+ is used with Boot 4.0+ and requires Java 21.
 *
 * @author Moritz Halbritter
 */
public class TimefoldVersionProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	@Override
	public int getOrder() {
		return JavaVersionProjectDescriptionCustomizer.ORDER + 10;
	}

	@Override
	public void customize(MutableProjectDescription description) {
		Set<String> dependencyIds = description.getRequestedDependencies().keySet();
		if (!dependencyIds.contains("timefold-solver")) {
			return;
		}
		Language language = description.getLanguage();
		Integer javaGeneration = determineJavaGeneration(language.jvmVersion());
		if (javaGeneration != null && javaGeneration < 21) {
			Language compatibleLanguage = Language.forId(description.getLanguage().id(), "21");
			description.setLanguage(compatibleLanguage);
			description.getChanges()
				.add(ProjectDescriptionField.JVM_VERSION,
						"The JVM level was changed to '21' as Timefold requires Java 21 or later.");
		}
	}

	private Integer determineJavaGeneration(String javaVersion) {
		try {
			return Integer.parseInt(javaVersion);
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

}
