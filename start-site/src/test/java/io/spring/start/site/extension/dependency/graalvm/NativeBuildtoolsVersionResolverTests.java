/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.graalvm;

import java.util.stream.Stream;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NativeBuildtoolsVersionResolver}.
 *
 * @author Stephane Nicoll
 */
class NativeBuildtoolsVersionResolverTests {

	@ParameterizedTest
	@MethodSource("platformVersions")
	void resolveNativeBuildToolsVersion(Version platformVersion, String expectedBuildToolsVersion) {
		assertThat(NativeBuildtoolsVersionResolver.resolve(platformVersion)).isEqualTo(expectedBuildToolsVersion);
	}

	private static Stream<Arguments> platformVersions() {
		return Stream.of(versions("2.7.0", null), versions("3.0.0-M1", "0.9.14"), versions("3.0.0-RC1", "0.9.16"),
				versions("3.0.0-RC2", "0.9.17"), versions("3.0.0", "0.9.18"), versions("3.0.1", "0.9.19"),
				versions("3.0.3", "0.9.20"), versions("3.1.0-SNAPSHOT", "0.9.20"));
	}

	private static Arguments versions(String platformVersion, String nativeBuildToolsVersion) {
		return Arguments.of(VersionParser.DEFAULT.parse(platformVersion), nativeBuildToolsVersion);
	}

}
