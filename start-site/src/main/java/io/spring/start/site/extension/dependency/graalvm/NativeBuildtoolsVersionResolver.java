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

import java.util.List;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Resolve the Native Build Tools version to use based on the platform version.
 *
 * @author Stephane Nicoll
 */
abstract class NativeBuildtoolsVersionResolver {

	private static final List<NativeBuildtoolsRange> ranges = List.of(
			new NativeBuildtoolsRange("[3.0.0-M1,3.0.0-RC1)", "0.9.14"),
			new NativeBuildtoolsRange("[3.0.0-RC1,3.0.0-RC2)", "0.9.16"),
			new NativeBuildtoolsRange("[3.0.0-RC2,3.0.0)", "0.9.17"),
			new NativeBuildtoolsRange("[3.0.0,3.0.1)", "0.9.18"), new NativeBuildtoolsRange("[3.0.1,3.0.3)", "0.9.19"),
			new NativeBuildtoolsRange("[3.0.3,3.1.0-M1)", "0.9.20"), new NativeBuildtoolsRange("3.1.0-M1", "0.9.20"));

	static String resolve(Version platformVersion) {
		return ranges.stream()
			.filter((range) -> range.match(platformVersion))
			.findFirst()
			.map(NativeBuildtoolsRange::getVersion)
			.orElse(null);
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
