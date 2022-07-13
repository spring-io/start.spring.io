/*
 * Copyright 2012-2021 the original author or authors.
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

package io.spring.start.site.extension.dependency.springnative;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringNativeBuildtoolsVersionResolver}.
 *
 * @author Stephane Nicoll
 */
class SpringNativeBuildtoolsVersionResolverTests {

	@ParameterizedTest
	@MethodSource("springNativeVersions")
	void resolveNativeBuildToolsVersion(String springNativeVersion, String expectedBuildToolsVersion) {
		assertThat(SpringNativeBuildtoolsVersionResolver.resolve(springNativeVersion))
				.isEqualTo(expectedBuildToolsVersion);
	}

	private static Stream<Arguments> springNativeVersions() {
		return Stream.of(versions("0.9.2", null), versions("0.10.4", "0.9.3"), versions("0.10.5", "0.9.4"),
				versions("0.11.0-M1", "0.9.4"), versions("0.11.0-M2", "0.9.7.1"), versions("0.11.0-RC1", "0.9.8"),
				versions("0.11.0", "0.9.8"), versions("0.11.1", "0.9.9"), versions("0.11.3", "0.9.10"),
				versions("0.11.5", "0.9.11"), versions("0.12.0", "0.9.11"), versions("0.12.1", "0.9.13"));
	}

	private static Arguments versions(String springNativeVersion, String nativeBuildToolsVersion) {
		return Arguments.of(springNativeVersion, nativeBuildToolsVersion);
	}

}
