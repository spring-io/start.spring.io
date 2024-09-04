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

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} for Gradle Groovy DSL projects using DGS Codegen.
 *
 * @author Brian Clozel
 */
class DgsCodegenGroovyDslGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final String pluginVersion;

	private final String packageName;

	DgsCodegenGroovyDslGradleBuildCustomizer(String pluginVersion, String packageName) {
		this.pluginVersion = pluginVersion;
		this.packageName = packageName;
	}

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("com.netflix.dgs.codegen", (plugin) -> plugin.setVersion(this.pluginVersion));
		build.extensions().customize("generateJava", (generateJava) -> {
			generateJava.attribute("schemaPaths", "[\"${projectDir}/src/main/resources/graphql-client\"]");
			generateJava.attribute("packageName", "'" + this.packageName + ".codegen'");
			generateJava.attribute("generateClient", "true");
		});
	}

}
