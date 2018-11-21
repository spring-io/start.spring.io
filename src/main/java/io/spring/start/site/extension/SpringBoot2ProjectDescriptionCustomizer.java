/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import java.util.Arrays;
import java.util.List;

import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.version.Version;

/**
 * As of Spring Boot 2.0, Java8 is mandatory so this extension makes sure that the java
 * version is forced.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 */
class SpringBoot2ProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private static final Version VERSION_2_0_0_M1 = Version.parse("2.0.0.M1");

	private static final List<String> VALID_VERSIONS = Arrays.asList("1.8", "9", "10",
			"11");

	@Override
	public void customize(ProjectDescription description) {
		if (!VALID_VERSIONS.contains(description.getLanguage().jvmVersion())
				&& isSpringBootVersionAtLeastAfter(description)) {
			description
					.setLanguage(Language.forId(description.getLanguage().id(), "1.8"));
		}
	}

	private boolean isSpringBootVersionAtLeastAfter(ProjectDescription description) {
		return (VERSION_2_0_0_M1.compareTo(description.getPlatformVersion()) <= 0);
	}

}
