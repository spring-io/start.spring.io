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

package io.spring.start.site.project;

import java.util.Arrays;
import java.util.List;

import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Validate that the requested java version is compatible with the chosen Spring Boot
 * generation and adapt the request if necessary.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 * @author Moritz Halbritter
 */
public class JavaVersionProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private static final VersionRange SPRING_BOOT_3_4_OR_LATER = VersionParser.DEFAULT.parseRange("3.4.0");

	private static final List<String> UNSUPPORTED_VERSIONS = Arrays.asList("1.6", "1.7", "1.8");

	@Override
	public void customize(MutableProjectDescription description) {
		String javaVersion = description.getLanguage().jvmVersion();
		if (UNSUPPORTED_VERSIONS.contains(javaVersion)) {
			updateTo(description, "17");
			return;
		}
		Integer javaGeneration = determineJavaGeneration(javaVersion);
		if (javaGeneration == null) {
			return;
		}
		if (javaGeneration < 17) {
			updateTo(description, "17");
		}
		if (javaGeneration >= 22) {
			if (isKotlin(description)) {
				// Kotlin 1.9.x doesn't support Java > 21
				updateTo(description, "21");
			}
		}
		if (javaGeneration >= 24) {
			// Spring Boot < 3.4.x supports Java 23 at most
			Version platformVersion = description.getPlatformVersion();
			if (!SPRING_BOOT_3_4_OR_LATER.match(platformVersion)) {
				updateTo(description, "23");
			}
		}
	}

	private boolean isKotlin(MutableProjectDescription description) {
		return description.getLanguage() instanceof KotlinLanguage;
	}

	private void updateTo(MutableProjectDescription description, String jvmVersion) {
		Language language = Language.forId(description.getLanguage().id(), jvmVersion);
		description.setLanguage(language);
	}

	private Integer determineJavaGeneration(String javaVersion) {
		try {
			int generation = Integer.parseInt(javaVersion);
			return (generation > 9 && generation <= 24) ? generation : null;
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

}
