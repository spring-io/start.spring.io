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

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} for Maven projects using DGS Codegen (community maintained).
 *
 * @author Brian Clozel
 * @see <a href="https://github.com/deweyjose/graphqlcodegen">Maven GraphQL Codegen
 * plugin</a>
 */
public class DgsCodegenMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private final String pluginVersion;

	private final String packageName;

	public DgsCodegenMavenBuildCustomizer(String pluginVersion, String packageName) {
		this.pluginVersion = pluginVersion;
		this.packageName = packageName;
	}

	@Override
	public void customize(MavenBuild build) {
		build.plugins()
			.add("io.github.deweyjose", "graphqlcodegen-maven-plugin",
					(plugin) -> plugin.version(this.pluginVersion)
						.execution("dgs-codegen",
								(execution) -> execution.goal("generate")
									.configuration((configuration) -> configuration
										.add("schemaPaths",
												(schemaPaths) -> schemaPaths.add("param",
														"src/main/resources/graphql-client"))
										.add("packageName", this.packageName + ".codegen")
										.add("addGeneratedAnnotation", "true"))));
		build.plugins()
			.add("org.codehaus.mojo", "build-helper-maven-plugin",
					(plugin) -> plugin.execution("add-dgs-source", (execution) -> execution.goal("add-source")
						.phase("generate-sources")
						.configuration((configuration) -> configuration.add("sources",
								(sources) -> sources.add("source", "${project.build.directory}/generated-sources")))));
	}

}
