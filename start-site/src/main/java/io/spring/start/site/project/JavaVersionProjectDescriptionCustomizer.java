/*
 * Copyright 2012-2022 the original author or authors.
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
import java.util.function.Consumer;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.language.groovy.GroovyLanguage;
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

	private static final VersionRange SPRING_BOOT_2_3_0_OR_LATER = VersionParser.DEFAULT.parseRange("2.3.0.RELEASE");

	private static final VersionRange SPRING_BOOT_2_4_4_OR_LATER = VersionParser.DEFAULT.parseRange("2.4.4");

	private static final VersionRange SPRING_BOOT_2_5_0_OR_LATER = VersionParser.DEFAULT.parseRange("2.5.0-RC1");

	private static final VersionRange SPRING_BOOT_2_5_5_OR_LATER = VersionParser.DEFAULT.parseRange("2.5.5");

	private static final VersionRange SPRING_BOOT_2_5_11_OR_LATER = VersionParser.DEFAULT.parseRange("2.5.11");

	private static final VersionRange SPRING_BOOT_2_6_0_OR_LATER = VersionParser.DEFAULT.parseRange("2.6.0");

	private static final VersionRange SPRING_BOOT_2_6_12_OR_LATER = VersionParser.DEFAULT.parseRange("2.6.12");

	private static final VersionRange SPRING_BOOT_3_0_0_OR_LATER = VersionParser.DEFAULT.parseRange("3.0.0-M1");

	private static final VersionRange GRADLE_6 = VersionParser.DEFAULT.parseRange("2.2.2.RELEASE");

	private static final VersionRange Spring_NATIVE_011 = VersionParser.DEFAULT.parseRange("2.6.0-M3");

	@Override
	public void customize(MutableProjectDescription description) {
		String javaVersion = description.getLanguage().jvmVersion();
		if (UNSUPPORTED_VERSIONS.contains(javaVersion)) {
			updateTo(description, "1.8");
			return;
		}
		springNativeHandler().accept(description);
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

		// 13 support only as of Gradle 6
		if (javaGeneration == 13 && description.getBuildSystem() instanceof GradleBuildSystem
				&& !GRADLE_6.match(platformVersion)) {
			updateTo(description, "11");
		}
		// 15 support only as of 2.2.11
		if (javaGeneration == 15 && !SPRING_BOOT_2_3_0_OR_LATER.match(platformVersion)) {
			updateTo(description, "11");
		}
		if (javaGeneration == 16) {
			// Full Support as of Spring Boot 2.5 only.
			// 16 support as of Kotlin 1.5
			if (description.getLanguage() instanceof KotlinLanguage
					&& !SPRING_BOOT_2_5_0_OR_LATER.match(platformVersion)) {
				updateTo(description, "11");
			}
			// 16 support as of Gradle 7
			if (description.getBuildSystem() instanceof GradleBuildSystem
					&& !SPRING_BOOT_2_5_0_OR_LATER.match(platformVersion)) {
				updateTo(description, "11");
			}
			// Groovy 3 only available as of 2.5
			if (description.getLanguage() instanceof GroovyLanguage
					&& !SPRING_BOOT_2_5_0_OR_LATER.match(platformVersion)) {
				updateTo(description, "11");
			}
			// 16 support only as of 2.4.4
			if (!SPRING_BOOT_2_4_4_OR_LATER.match(platformVersion)) {
				updateTo(description, "11");
			}
		}
		if (javaGeneration == 17) {
			// Java 17 support as of Spring Boot 2.5.5
			if (!SPRING_BOOT_2_5_5_OR_LATER.match(platformVersion)) {
				updateTo(description, "11");
			}
			// Kotlin 1.6 only
			if (description.getLanguage() instanceof KotlinLanguage
					&& !SPRING_BOOT_2_6_0_OR_LATER.match(platformVersion)) {
				updateTo(description, "11");
			}
		}
		if (javaGeneration == 18) {
			// Java 17 support as of Spring Boot 2.5.11
			if (!SPRING_BOOT_2_5_11_OR_LATER.match(platformVersion)) {
				updateTo(description, "17");
			}
			// Kotlin support to be determined
			if (description.getLanguage() instanceof KotlinLanguage) {
				if (!SPRING_BOOT_2_6_0_OR_LATER.match(platformVersion)) {
					updateTo(description, "11");
				}
				else {
					updateTo(description, "17");
				}
			}
		}
		if (javaGeneration == 19) {
			// Java 18 support as of Spring Boot 2.6.12
			if (!SPRING_BOOT_2_6_12_OR_LATER.match(platformVersion)) {
				updateTo(description, "17");
			}
			// Kotlin support to be determined
			if (description.getLanguage() instanceof KotlinLanguage) {
				if (!SPRING_BOOT_2_6_0_OR_LATER.match(platformVersion)) {
					updateTo(description, "11");
				}
				else {
					updateTo(description, "17");
				}
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
			return ((generation > 8 && generation <= 19) ? generation : null);
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

	private Consumer<MutableProjectDescription> springNativeHandler() {
		return (description) -> {
			if (!description.getRequestedDependencies().containsKey("native")) {
				return;
			}
			if ("1.8".equals(description.getLanguage().jvmVersion())
					&& Spring_NATIVE_011.match(description.getPlatformVersion())) {
				updateTo(description, "11");
			}
		};
	}

}
