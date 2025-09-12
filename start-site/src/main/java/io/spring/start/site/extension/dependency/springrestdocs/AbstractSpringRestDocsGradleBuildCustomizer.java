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

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Abstract {@link BuildCustomizer Customizer} for a {@link GradleBuild} when the
 * generated project depends on Spring REST Docs.
 *
 * @author Moritz Halbritter
 */
abstract class AbstractSpringRestDocsGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("org.asciidoctor.jvm.convert", (plugin) -> plugin.setVersion("3.3.2"));
		build.properties().property("snippetsDir", "file(\"build/generated-snippets\")");
		customizeForDialect(build);
	}

	abstract void customizeForDialect(GradleBuild build);

}
