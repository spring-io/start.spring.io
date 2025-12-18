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

package io.spring.start.site.container;

import io.spring.initializr.generator.version.Version;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Testcontainers}.
 *
 * @author Moritz Halbritter
 */
class TestcontainersTests {

	@CsvSource(textBlock = """
			3.5.0,			false
			4.0.0-M1,		false
			4.0.0-M2,		false
			4.0.0-M3,		false
			4.0.0-RC1,		true
			4.0.0-SNAPSHOT,	true
			4.0.0,			true
			""")
	@ParameterizedTest
	void shouldUseBootVersionDoDetermineIfTestcontainers2IsUsed(String bootVersion, boolean hasTc2) {
		Version version = Version.parse(bootVersion);
		assertThat(new Testcontainers(version).hasTc2()).isEqualTo(hasTc2);
	}

	@Test
	void shouldResolveArtifactId() {
		assertThat(new Testcontainers(Version.parse("4.0.0")).resolveArtifactId("postgresql"))
			.isEqualTo("testcontainers-postgresql");
		assertThat(new Testcontainers(Version.parse("3.5.0")).resolveArtifactId("postgresql")).isEqualTo("postgresql");
	}

}
