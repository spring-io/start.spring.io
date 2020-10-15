/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.start.site.extension.code.groovy;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Groovy 3 {@link BuildCustomizer} to support more recent JVM versions.
 *
 * @author Stephane Nicoll
 */
public class Groovy3CompatibilityBuildCustomer implements BuildCustomizer<Build> {

	private final String jvmVersion;

	public Groovy3CompatibilityBuildCustomer(String jvmVersion) {
		this.jvmVersion = jvmVersion;
	}

	@Override
	public void customize(Build build) {
		Integer javaGeneration = determineJavaGeneration(this.jvmVersion);
		if (javaGeneration != null && javaGeneration >= 14) {
			build.properties().version("groovy.version", "3.0.6");
		}
	}

	private static Integer determineJavaGeneration(String javaVersion) {
		try {
			return Integer.parseInt(javaVersion);
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

}
