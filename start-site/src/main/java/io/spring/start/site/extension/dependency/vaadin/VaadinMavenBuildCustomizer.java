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

package io.spring.start.site.extension.dependency.vaadin;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that adds a production profile to enable Vaadin's production
 * mode.
 *
 * @author Stephane Nicoll
 */
class VaadinMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		build.profiles().id("production").plugins().add("com.vaadin", "vaadin-maven-plugin",
				(plugin) -> plugin.version("${vaadin.version}").execution("frontend",
						(execution) -> execution.goal("prepare-frontend").goal("build-frontend").phase("compile")
								.configuration((configuration) -> configuration.add("productionMode", "true"))));
	}

}
