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
 */
public class JavaVersionProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private static final List<String> UNSUPPORTED_VERSIONS = Arrays.asList("1.6", "1.7");

	private static final VersionRange SPRING_BOOT_2_6_12_OR_LATER = VersionParser.DEFAULT.parseRange("2.6.12");

	private static final VersionRange SPRING_BOOT_2_7_10_OR_LATER = VersionParser.DEFAULT.parseRange("2.7.10");

	private static final VersionRange SPRING_BOOT_2_7_16_OR_LATER = VersionParser.DEFAULT.parseRange("2.7.16");

	private static final VersionRange SPRING_BOOT_3_0_0_OR_LATER = VersionParser.DEFAULT.parseRange("3.0.0-M1");

	@Override
	public void customize(MutableProjectDescription description) {
		String javaVersion = description.getLanguage().jvmVersion();
		if (UNSUPPORTED_VERSIONS.contains(javaVersion)) {
			updateTo(description, "1.8");
			return;
		}
		Integer javaGeneration = determineJavaGeneration(javaVersion);
		if (javaGeneration == null) {
			return;
		}
		Version platformVersion = description.getPlatformVersion();
		// Spring Boot 3 requires Java 17
		if (javaGeneration < 17 && SPRING_BOOT_3_0_0_OR_LATER.match(platformVersion)) {
			updateTo(description, "17");
			return;
		}
		if (javaGeneration == 19) {
			// Java 19 support as of Spring Boot 2.6.12
			if (!SPRING_BOOT_2_6_12_OR_LATER.match(platformVersion)) {
				updateTo(description, "17");
			}
		}
		if (javaGeneration == 20) {
			// Java 20 support as of Spring Boot 2.7.10
			if (!SPRING_BOOT_2_7_10_OR_LATER.match(platformVersion)) {
				updateTo(description, "17");
			}

		}
		if (javaGeneration == 21) {
			// Java 21 support as of Spring Boot 2.7.16
			if (!SPRING_BOOT_2_7_16_OR_LATER.match(platformVersion)) {
				updateTo(description, "17");
			}
			// Kotlin does not support Java 21 bytecodes yet
			if (description.getLanguage() instanceof KotlinLanguage) {
				updateTo(description, "17");
			}
		}
	}

	private void updateTo(MutableProjectDescription description, String jvmVersion) {
		Language language = Language.forId(description.getLanguage().id(), jvmVersion);
		description.setLanguage(language);
	}

	private Integer determineJavaGeneration(String javaVersion) {
		try {
			if ("1.8".equals(javaVersion)) {
				return 8;
			}
			int generation = Integer.parseInt(javaVersion);
			return ((generation > 8 && generation <= 21) ? generation : null);
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

}
