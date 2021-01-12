/*
 * Copyright 2012-2021 the original author or authors.
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

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.language.Language;
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

	private static final VersionRange SPRING_BOOT_2_2_6_OR_LATER = VersionParser.DEFAULT.parseRange("2.2.6.RELEASE");

	private static final VersionRange SPRING_BOOT_2_2_11_OR_LATER = VersionParser.DEFAULT.parseRange("2.2.11.RELEASE");

	private static final VersionRange GRADLE_6 = VersionParser.DEFAULT.parseRange("2.2.2.RELEASE");

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
		// 13 support only as of Gradle 6
		if (javaGeneration == 13 && description.getBuildSystem() instanceof GradleBuildSystem
				&& !GRADLE_6.match(platformVersion)) {
			updateTo(description, "11");
		}
		// 14 support only as of 2.2.6
		if (javaGeneration == 14 && !SPRING_BOOT_2_2_6_OR_LATER.match(platformVersion)) {
			updateTo(description, "11");
		}
		// 15 support only as of 2.2.11
		if (javaGeneration == 15 && !SPRING_BOOT_2_2_11_OR_LATER.match(platformVersion)) {
			updateTo(description, "11");
		}
	}

	private void updateTo(MutableProjectDescription description, String jvmVersion) {
		Language language = Language.forId(description.getLanguage().id(), jvmVersion);
		description.setLanguage(language);
	}

	private Integer determineJavaGeneration(String javaVersion) {
		try {
			int generation = Integer.parseInt(javaVersion);
			return ((generation > 8 && generation <= 15) ? generation : null);
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

}
