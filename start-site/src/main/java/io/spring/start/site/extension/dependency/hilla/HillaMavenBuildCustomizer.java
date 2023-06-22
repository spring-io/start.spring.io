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

package io.spring.start.site.extension.dependency.hilla;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that adds a production profile to enable Hilla's production
 * mode.
 *
 * @author Luciano Vernaschi
 * @author Stephane Nicoll
 */
class HillaMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		build.plugins()
			.add("dev.hilla", "hilla-maven-plugin", (plugin) -> plugin.version("${hilla.version}")
				.execution("frontend", (execution) -> execution.goal("prepare-frontend")));
		build.profiles()
			.id("production")
			.plugins()
			.add("dev.hilla", "hilla-maven-plugin",
					(plugin) -> plugin.version("${hilla.version}")
						.execution("frontend",
								(execution) -> execution.goal("build-frontend")
									.phase("compile")
									.configuration((configuration) -> configuration.add("productionMode", "true"))));
	}

}
