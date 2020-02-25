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

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that tunes the REST Docs dependency based on the web
 * framework that the application is using.
 *
 * @author Andy Wilkinson
 */
public class SpringRestDocsBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (switchToWebTestClient(build)) {
			build.dependencies().remove("restdocs");
			build.dependencies().add("restdocs-webtestclient", "org.springframework.restdocs",
					"spring-restdocs-webtestclient", DependencyScope.TEST_COMPILE);
		}
	}

	private boolean switchToWebTestClient(Build build) {
		if (build.dependencies().has("web")) {
			return false;
		}
		if (build.dependencies().has("webflux") || build.dependencies().has("jersey")) {
			return true;
		}
		return false;
	}

}
