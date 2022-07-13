/*
 * Copyright 2012-2022 the original author or authors.
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

import java.util.Arrays;
import java.util.List;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Resolve the Spring Native buildtools version to use based on the Spring Native version.
 *
 * @author Stephane Nicoll
 */
abstract class SpringNativeBuildtoolsVersionResolver {

	private static final List<NativeBuildtoolsRange> ranges = Arrays.asList(
			new NativeBuildtoolsRange("[0.9.0,0.10.0-M1)", null),
			new NativeBuildtoolsRange("[0.10.0-M1, 0.10.5)", "0.9.3"),
			new NativeBuildtoolsRange("[0.10.5, 0.11.0-M2)", "0.9.4"),
			new NativeBuildtoolsRange("[0.11.0-M2, 0.11.0-RC1)", "0.9.7.1"),
			new NativeBuildtoolsRange("[0.11.0-RC1, 0.11.1)", "0.9.8"),
			new NativeBuildtoolsRange("[0.11.1,0.11.3)", "0.9.9"),
			new NativeBuildtoolsRange("[0.11.3,0.11.4)", "0.9.10"),
			new NativeBuildtoolsRange("[0.11.5,0.12.1)", "0.9.11"), new NativeBuildtoolsRange("0.12.1", "0.9.13"));

	static String resolve(String springNativeVersion) {
		Version nativeVersion = VersionParser.DEFAULT.parse(springNativeVersion);
		return ranges.stream().filter((range) -> range.match(nativeVersion)).findFirst()
				.map(NativeBuildtoolsRange::getVersion).orElse(null);
	}

	private static class NativeBuildtoolsRange {

		private final VersionRange range;

		private final String version;

		NativeBuildtoolsRange(String range, String version) {
			this.range = parseRange(range);
			this.version = version;
		}

		String getVersion() {
			return this.version;
		}

		private static VersionRange parseRange(String s) {
			return VersionParser.DEFAULT.parseRange(s);
		}

		boolean match(Version version) {
			return this.range.match(version);
		}

	}

}
