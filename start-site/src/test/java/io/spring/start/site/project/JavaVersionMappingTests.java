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

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JavaVersionMapping}.
 *
 * @author Moritz Halbritter
 */
class JavaVersionMappingTests {

	private final JavaVersionMapping mapping = new JavaVersionMapping();

	@CsvSource(textBlock = """
			4.0.0,17,26
			4.1.0,17,26
			""")
	@ParameterizedTest(name = "Spring Boot {0} | min {1} | max {2}")
	void javaVersions(String bootVersion, int expectedJavaMin, int expectedJavaMax) {
		Version version = toVersion(bootVersion);
		assertThat(this.mapping.getMinJavaVersion(version)).isEqualTo(expectedJavaMin);
		assertThat(this.mapping.getMaxJavaVersion(version)).isEqualTo(expectedJavaMax);
	}

	@CsvSource(textBlock = """
			4.0.0,2.2.0
			4.1.0,2.3.0
			""")
	@ParameterizedTest(name = "Spring Boot {0} | Kotlin {1}")
	void kotlinVersion(String bootVersion, String expectedKotlinVersion) {
		assertThat(this.mapping.getKotlinVersion(toVersion(bootVersion))).isEqualTo(toVersion(expectedKotlinVersion));
	}

	private Version toVersion(String version) {
		return VersionParser.DEFAULT.parse(version);
	}

}
