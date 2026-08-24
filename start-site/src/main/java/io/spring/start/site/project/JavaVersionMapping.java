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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.log.LogMessage;

/**
 * Maps Spring Boot versions to minimum and maximum Java versions.
 *
 * @author Moritz Halbritter
 */
class JavaVersionMapping {

	private static final Log logger = LogFactory.getLog(JavaVersionMapping.class);

	private static final List<Mapping> mappings = List.of(Mapping.of("[4.0.0-M1,4.1.0-M1)", 17, 26, "2.2.0"),
			Mapping.of("[4.1.0-M1,4.2.0-M1)", 17, 26, "2.3.0"), Mapping.of("[4.2.0-M1,4.3.0-M1)", 17, 26, "2.4.0"));

	/**
	 * Returns the minimum supported Java version.
	 * @param springBootVersion the version of Spring Boot
	 * @return the minimum Java version
	 */
	int getMinJavaVersion(Version springBootVersion) {
		return findMapping(springBootVersion).minJavaVersion();
	}

	/**
	 * Returns the maximum supported Java version.
	 * @param springBootVersion the version of Spring Boot
	 * @return the maximum Java version
	 */
	int getMaxJavaVersion(Version springBootVersion) {
		return findMapping(springBootVersion).maxJavaVersion();
	}

	/**
	 * Returns the Kotlin version used by the given Spring Boot version.
	 * @param springBootVersion the version of Spring Boot
	 * @return the Kotlin version
	 */
	Version getKotlinVersion(Version springBootVersion) {
		return findMapping(springBootVersion).kotlinVersion();
	}

	private Mapping findMapping(Version springBootVersion) {
		for (Mapping mapping : mappings) {
			if (mapping.match(springBootVersion)) {
				return mapping;
			}
		}
		logger.warn(LogMessage.format("Failed to find mapping for Spring Boot %s", springBootVersion));
		return mappings.get(mappings.size() - 1);
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
