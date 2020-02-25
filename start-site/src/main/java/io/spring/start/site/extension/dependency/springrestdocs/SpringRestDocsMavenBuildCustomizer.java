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

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer Customizer} for a {@link MavenBuild} when the generated project
 * depends on Spring REST Docs.
 *
 * @author Andy Wilkinson
 */
class SpringRestDocsMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		build.plugins().add("org.asciidoctor", "asciidoctor-maven-plugin", (plugin) -> {
			plugin.version("1.5.8");
			plugin.execution("generate-docs", (execution) -> {
				execution.phase("prepare-package");
				execution.goal("process-asciidoc");
				execution.configuration((configuration) -> {
					configuration.add("backend", "html");
					configuration.add("doctype", "book");
				});
			});
			plugin.dependency("org.springframework.restdocs", "spring-restdocs-asciidoctor",
					"${spring-restdocs.version}");
		});
	}

}
