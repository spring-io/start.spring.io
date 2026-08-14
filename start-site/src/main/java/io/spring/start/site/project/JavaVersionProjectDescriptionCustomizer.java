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

import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.project.ProjectDescriptionField;
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

	/**
	 * Order of this customizer. A customizer that applies the constraints of a requested
	 * dependency has to run after this one, as such a dependency can require a more
	 * recent JVM version than the platform and should then have the last word.
	 */
	public static final int ORDER = 0;

	private static final String LEGACY_VERSION_PREFIX = "1.";

	private static final int MAX_JAVA_VERSION = 26;

	private static final String JDK_VERSION_RANGE_WIKI = "https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-Versions#jdk-version-range";

	private final JavaVersionMapping javaVersionMapping = new JavaVersionMapping();

	private final KotlinVersionMapping kotlinVersionMapping = new KotlinVersionMapping();

	@Override
	public int getOrder() {
		return ORDER;
	}

	@Override
	public void customize(MutableProjectDescription description) {
		Language language = description.getLanguage();
		Version bootVersion = description.getPlatformVersion();
		Integer javaGeneration = determineJavaGeneration(language.jvmVersion());
		if (javaGeneration == null) {
			return;
		}
		int constrainedJavaGeneration = constrainBySpringBoot(description, javaGeneration, bootVersion);
		if (language instanceof KotlinLanguage) {
			constrainByKotlin(description, constrainedJavaGeneration, bootVersion);
		}
	}

	private int constrainBySpringBoot(MutableProjectDescription description, int javaGeneration, Version bootVersion) {
		int minJavaVersion = this.javaVersionMapping.getMinJavaVersion(bootVersion);
		if (javaGeneration < minJavaVersion) {
			return updateToSpringBootVersion(description, minJavaVersion);
		}
		int maxJavaVersion = this.javaVersionMapping.getMaxJavaVersion(bootVersion);
		if (javaGeneration > maxJavaVersion) {
			return updateToSpringBootVersion(description, maxJavaVersion);
		}
		return javaGeneration;
	}

	private void constrainByKotlin(MutableProjectDescription description, int javaGeneration, Version bootVersion) {
		Version kotlinVersion = this.javaVersionMapping.getKotlinVersion(bootVersion);
		int minJavaVersion = this.kotlinVersionMapping.getMinJavaVersion(kotlinVersion);
		if (javaGeneration < minJavaVersion) {
			updateToKotlinVersion(description, minJavaVersion);
			return;
		}
		int maxJavaVersion = this.kotlinVersionMapping.getMaxJavaVersion(kotlinVersion);
		if (javaGeneration > maxJavaVersion) {
			updateToKotlinVersion(description, maxJavaVersion);
		}
	}

	private int updateToSpringBootVersion(MutableProjectDescription description, int jvmVersion) {
		updateTo(description, jvmVersion);
		description.getChanges()
			.add(ProjectDescriptionField.JVM_VERSION,
					"The JVM level was changed to '%s', review the [JDK Version Range](%s) on the wiki for more details."
						.formatted(jvmVersion, JDK_VERSION_RANGE_WIKI));
		return jvmVersion;
	}

	private void updateToKotlinVersion(MutableProjectDescription description, int jvmVersion) {
		updateTo(description, jvmVersion);
		description.getChanges()
			.add(ProjectDescriptionField.JVM_VERSION,
					"The JVM level was changed to '%s' as the Kotlin version does not support a later Java version yet."
						.formatted(jvmVersion));
	}

	private void updateTo(MutableProjectDescription description, int jvmVersion) {
		Language language = description.getLanguage();
		description.setLanguage(Language.forId(language.id(), Integer.toString(jvmVersion)));
	}

	private Integer determineJavaGeneration(String javaVersion) {
		// Java releases up to 8 are named '1.x'
		String generation = javaVersion.startsWith(LEGACY_VERSION_PREFIX)
				? javaVersion.substring(LEGACY_VERSION_PREFIX.length()) : javaVersion;
		try {
			int parsedGeneration = Integer.parseInt(generation);
			// A version beyond the ones we know about is left as is
			return (parsedGeneration <= MAX_JAVA_VERSION) ? parsedGeneration : null;
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

}
