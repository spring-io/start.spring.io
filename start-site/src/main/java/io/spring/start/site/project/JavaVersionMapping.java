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

import java.util.List;

import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Maps Spring Boot versions to minimum and maximum Java versions.
 *
 * @author Moritz Halbritter
 */
class JavaVersionMapping {

	private static final List<Mapping> mappings = List.of(Mapping.of("[3.5.0-M1,4.0.0-M1)", 17, 25, "1.9.25"),
			Mapping.of("[4.0.0-M1,4.1.0-M1)", 17, 25, "2.2.0"));

	private final KotlinVersionMapping kotlinMapping = new KotlinVersionMapping();

	/**
	 * Returns the minimum supported Java version.
	 * @param springBootVersion the version of Spring Boot
	 * @param language the project language
	 * @return the minimum Java version
	 */
	int getMinJavaVersion(Version springBootVersion, Language language) {
		Mapping mapping = findMapping(springBootVersion);
		int result = mapping.minJavaVersion();
		if (isKotlin(language)) {
			return Math.max(result, this.kotlinMapping.getMinJavaVersion(mapping.kotlinVersion()));
		}
		return result;
	}

	/**
	 * Returns the maximum supported Java version.
	 * @param springBootVersion the version of Spring Boot
	 * @param language the project language
	 * @return the maximum Java version
	 */
	int getMaxJavaVersion(Version springBootVersion, Language language) {
		Mapping mapping = findMapping(springBootVersion);
		int result = mapping.maxJavaVersion();
		if (isKotlin(language)) {
			return Math.min(result, this.kotlinMapping.getMaxJavaVersion(mapping.kotlinVersion()));
		}
		return result;
	}

	private boolean isKotlin(Language language) {
		return language instanceof KotlinLanguage;
	}

	private Mapping findMapping(Version springBootVersion) {
		for (Mapping mapping : mappings) {
			if (mapping.match(springBootVersion)) {
				return mapping;
			}
		}
		throw new IllegalStateException("Missing mapping for " + springBootVersion);
	}

	private record Mapping(VersionRange range, int minJavaVersion, int maxJavaVersion, Version kotlinVersion) {
		boolean match(Version springBootVersion) {
			return this.range.match(springBootVersion);
		}

		static Mapping of(String range, int minJavaVersion, int maxJavaVersion, String kotlinVersion) {
			return new Mapping(VersionParser.DEFAULT.parseRange(range), minJavaVersion, maxJavaVersion,
					VersionParser.DEFAULT.parse(kotlinVersion));
		}
	}

}
