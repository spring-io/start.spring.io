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
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.version.Version;

/**
 * Validate that the requested java version is compatible with the chosen Spring Boot
 * generation and adapt the request if necessary.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 * @author Moritz Halbritter
 */
public class JavaVersionProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private static final List<String> UNSUPPORTED_VERSIONS = Arrays.asList("1.6", "1.7", "1.8");

	private static final int MAX_JAVA_VERSION = 25;

	private final JavaVersionMapping mapping = new JavaVersionMapping();

	@Override
	public void customize(MutableProjectDescription description) {
		Language language = description.getLanguage();
		Version bootVersion = description.getPlatformVersion();
		int minSupported = this.mapping.getMinJavaVersion(bootVersion, language);
		int maxSupported = this.mapping.getMaxJavaVersion(bootVersion, language);
		String javaVersion = language.jvmVersion();
		if (UNSUPPORTED_VERSIONS.contains(javaVersion)) {
			updateTo(description, minSupported);
			return;
		}
		Integer javaGeneration = determineJavaGeneration(javaVersion);
		if (javaGeneration == null) {
			return;
		}
		if (javaGeneration < minSupported) {
			updateTo(description, minSupported);
		}
		if (javaGeneration > maxSupported) {
			updateTo(description, maxSupported);
		}
	}

	private void updateTo(MutableProjectDescription description, int jvmVersion) {
		Language language = Language.forId(description.getLanguage().id(), Integer.toString(jvmVersion));
		description.setLanguage(language);
	}

	private Integer determineJavaGeneration(String javaVersion) {
		try {
			int generation = Integer.parseInt(javaVersion);
			return (generation >= 9 && generation <= MAX_JAVA_VERSION) ? generation : null;
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

}
