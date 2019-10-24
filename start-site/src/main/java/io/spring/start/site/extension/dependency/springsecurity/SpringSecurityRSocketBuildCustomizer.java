/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.dependency.springsecurity;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that provides Spring Security's RSocket support when both
 * RSocket and Spring Security are selected.
 *
 * @author Stephane Nicoll
 */
public class SpringSecurityRSocketBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (build.dependencies().has("rsocket")) {
			build.dependencies().add("security-rsocket",
					Dependency.withCoordinates("org.springframework.security", "spring-security-rsocket"));
			build.dependencies().add("security-messaging",
					Dependency.withCoordinates("org.springframework.security", "spring-security-messaging"));
		}
	}

}
