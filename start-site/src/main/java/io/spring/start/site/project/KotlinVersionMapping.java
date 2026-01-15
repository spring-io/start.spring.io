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

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Maps Kotlin versions to minimum and maximum Java versions.
 *
 * @author Moritz Halbritter
 */
class KotlinVersionMapping {

	private static final List<Mapping> mappings = List.of(Mapping.of("[1.9.0,1.9.20)", 17, 20),
			Mapping.of("[1.9.20,2.0.0)", 17, 21), Mapping.of("[2.0.0,2.1.0)", 17, 22),
			Mapping.of("[2.1.0,2.2.0)", 17, 23), Mapping.of("[2.2.0,2.3.0)", 17, 24),
			Mapping.of("[2.3.0,2.4.0)", 17, 25));

	/**
	 * Returns the minimum supported Java version.
	 * @param kotlinVersion the version kotlin
	 * @return the minimum Java version
	 */
	int getMinJavaVersion(Version kotlinVersion) {
		return findMapping(kotlinVersion).minJavaVersion();
	}

	/**
	 * Returns the minimum supported Java version.
	 * @param kotlinVersion the version kotlin
	 * @return the maximum Java version
	 */
	int getMaxJavaVersion(Version kotlinVersion) {
		return findMapping(kotlinVersion).maxJavaVersion();
	}

	private Mapping findMapping(Version kotlinVersion) {
		for (Mapping mapping : mappings) {
			if (mapping.match(kotlinVersion)) {
				return mapping;
			}
		}
		throw new IllegalStateException("Missing mapping for " + kotlinVersion);
	}

	private record Mapping(VersionRange range, int minJavaVersion, int maxJavaVersion) {
		boolean match(Version kotlinVersion) {
			return this.range.match(kotlinVersion);
		}

		static Mapping of(String range, int minJavaVersion, int maxJavaVersion) {
			return new Mapping(VersionParser.DEFAULT.parseRange(range), minJavaVersion, maxJavaVersion);
		}

	}

}
