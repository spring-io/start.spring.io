/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import java.util.Arrays;
import java.util.List;

import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.version.Version;

/**
 *
 * Validate that the requested java version is compatible with the chosen Spring Boot
 * generation and adapt the request if necessary.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 */
public class JavaVersionProjectDescriptionCustomizer
		implements ProjectDescriptionCustomizer {

	private static final Version VERSION_2_0_0_M1 = Version.parse("2.0.0.M1");

	private static final Version VERSION_2_0_1 = Version.parse("2.0.1.RELEASE");

	private static final Version VERSION_2_1_0_M1 = Version.parse("2.1.0.M1");

	private static final List<String> UNSUPPORTED_LANGUAGES = Arrays.asList("groovy",
			"kotlin");

	@Override
	public void customize(ProjectDescription description) {
		Integer javaGeneration = determineJavaGeneration(
				description.getLanguage().jvmVersion());
		if (javaGeneration == null) {
			return;
		}
		// Not supported for Spring Boot 1.x
		if (isSpringBootVersionBefore(description, VERSION_2_0_0_M1)) {
			updateTo(description, "1.8");
		}
		// Not supported for Kotlin & Groovy
		if (UNSUPPORTED_LANGUAGES.contains(description.getLanguage().id())) {
			updateTo(description, "1.8");
		}
		// 10 support only as of 2.0.1
		if (javaGeneration == 10
				&& isSpringBootVersionBefore(description, VERSION_2_0_1)) {
			updateTo(description, "1.8");
		}
		// 11 support only as of 2.1.x
		if (javaGeneration == 11
				&& isSpringBootVersionBefore(description, VERSION_2_1_0_M1)) {
			updateTo(description, "1.8");
		}
	}

	private void updateTo(ProjectDescription projectDescription, String jvmVersion) {
		Language language = Language.forId(projectDescription.getLanguage().id(),
				jvmVersion);
		projectDescription.setLanguage(language);
	}

	private Integer determineJavaGeneration(String javaVersion) {
		try {
			int generation = Integer.valueOf(javaVersion);
			return ((generation > 8 && generation <= 11) ? generation : null);
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

	protected boolean isSpringBootVersionBefore(ProjectDescription description,
			Version version) {
		return version.compareTo(description.getPlatformVersion()) > 0;
	}

}
