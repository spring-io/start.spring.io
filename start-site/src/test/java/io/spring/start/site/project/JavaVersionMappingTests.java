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
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
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
			3.5.0,17,25
			4.0.0,17,25
			""")
	@ParameterizedTest(name = "Spring Boot {0} with Java | min {1} | max {2}")
	void java(String bootVersion, int expectedJavaMin, int expectedJavaMax) {
		Version version = toVersion(bootVersion);
		Language java = new JavaLanguage();
		int min = this.mapping.getMinJavaVersion(version, java);
		int max = this.mapping.getMaxJavaVersion(version, java);
		assertThat(min).isEqualTo(expectedJavaMin);
		assertThat(max).isEqualTo(expectedJavaMax);
	}

	@CsvSource(textBlock = """
			3.5.0,17,21
			4.0.0,17,24
			""")
	@ParameterizedTest(name = "Spring Boot {0} with Kotlin | min {1} | max {2}")
	void kotlin(String bootVersion, int expectedJavaMin, int expectedJavaMax) {
		Version version = toVersion(bootVersion);
		Language kotlin = new KotlinLanguage();
		int min = this.mapping.getMinJavaVersion(version, kotlin);
		int max = this.mapping.getMaxJavaVersion(version, kotlin);
		assertThat(min).isEqualTo(expectedJavaMin);
		assertThat(max).isEqualTo(expectedJavaMax);
	}

	private Version toVersion(String version) {
		return VersionParser.DEFAULT.parse(version);
	}

}
