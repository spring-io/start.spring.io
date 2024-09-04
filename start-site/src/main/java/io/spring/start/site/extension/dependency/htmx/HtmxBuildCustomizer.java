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

package io.spring.start.site.extension.dependency.htmx;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that replaces {@code htmx-spring-boot} with
 * {@code htmx-spring-boot-thymeleaf} if Thymeleaf is selected.
 *
 * @author Moritz Halbritter
 */
class HtmxBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (build.dependencies().has("thymeleaf")) {
			Dependency htmx = build.dependencies().get("htmx");
			build.dependencies().remove("htmx");
			build.dependencies()
				.add("htmx-thymeleaf",
						Dependency.withCoordinates(htmx.getGroupId(), "htmx-spring-boot-thymeleaf")
							.version(htmx.getVersion())
							.build());
		}
	}

}
