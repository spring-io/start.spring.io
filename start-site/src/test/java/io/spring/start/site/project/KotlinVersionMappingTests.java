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
 * Tests for {@link KotlinVersionMapping}.
 *
 * @author Moritz Halbritter
 */
class KotlinVersionMappingTests {

	private final KotlinVersionMapping mapping = new KotlinVersionMapping();

	@CsvSource(textBlock = """
			1.9.0,17,20
			1.9.20,17,21
			2.0.0,17,22
			2.1.0,17,23
			2.2.0,17,24
			2.3.0,17,25
			""")
	@ParameterizedTest(name = "Kotlin {0} | min {1} | max {2}")
	void test(String kotlinVersion, int expectedJavaMin, int expectedJavaMax) {
		Version version = toVersion(kotlinVersion);
		int min = this.mapping.getMinJavaVersion(version);
		int max = this.mapping.getMaxJavaVersion(version);
		assertThat(min).isEqualTo(expectedJavaMin);
		assertThat(max).isEqualTo(expectedJavaMax);
	}

	private Version toVersion(String version) {
		return VersionParser.DEFAULT.parse(version);
	}

}
