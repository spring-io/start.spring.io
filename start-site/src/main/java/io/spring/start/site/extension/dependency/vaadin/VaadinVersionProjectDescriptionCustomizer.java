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

package io.spring.start.site.extension.dependency.vaadin;

import java.util.Collection;

import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Validate that the requested java version is compatible with the chosen Spring Boot
 * generation and adapt the request if necessary.
 * <p>
 * Vaadin 25.0+ requires Java 21.
 *
 * @author Stephane Nicoll
 */
public class VaadinVersionProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private static final VersionRange SPRING_BOOT_4_OR_LATER = VersionParser.DEFAULT.parseRange("4.0.0");

	@Override
	public void customize(MutableProjectDescription description) {
		Collection<String> dependencyIds = description.getRequestedDependencies().keySet();
		if (!dependencyIds.contains("vaadin")) {
			return;
		}
		Version bootVersion = description.getPlatformVersion();
		if (SPRING_BOOT_4_OR_LATER.match(bootVersion)) {
			Language language = description.getLanguage();
			Integer javaGeneration = determineJavaGeneration(language.jvmVersion());
			if (javaGeneration != null && javaGeneration < 21) {
				Language compatibleLanguage = Language.forId(description.getLanguage().id(), "21");
				description.setLanguage(compatibleLanguage);
			}
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
