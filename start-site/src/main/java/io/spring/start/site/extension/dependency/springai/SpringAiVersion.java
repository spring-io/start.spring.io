/*
 * Copyright 2012-2024 the original author or authors.
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

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.initializr.metadata.InitializrMetadata;

final class SpringAiVersion {

	private static final VersionRange SPRING_AI_1_0_0_OR_LATER = VersionParser.DEFAULT.parseRange("1.0.0-M2");

	private SpringAiVersion() {

	}

	static boolean version1OrLater(InitializrMetadata metadata, Version platformVersion) {
		var springAiBomVersion = metadata.getConfiguration()
			.getEnv()
			.getBoms()
			.get("spring-ai")
			.resolve(platformVersion)
			.getVersion();
		return SPRING_AI_1_0_0_OR_LATER.match(Version.parse(springAiBomVersion));
	}

}
