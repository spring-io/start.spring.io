/*
 * Copyright 2012 - present the original author or authors.
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

package io.spring.start.site.extension.dependency.jte;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to configure the JTE Maven plugin.
 *
 * @author Moritz Halbritter
 */
class JteMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		Dependency jteStarter = build.dependencies().get("jte");
		build.plugins().add("gg.jte", "jte-maven-plugin", (plugin) -> {
			plugin.version(jteStarter.getVersion().getValue());
			plugin.execution("jte-generate", (execution) -> {
				execution.phase("generate-sources");
				execution.goal("generate");
				execution.configuration((configuration) -> {
					configuration.add("sourceDirectory", "${project.basedir}/src/main/jte");
					configuration.add("contentType", "Html");
					configuration.add("binaryStaticContent", "true");
					configuration.add("targetResourceDirectory", "${project.build.outputDirectory}");
				});
			});
		});
	}

}
