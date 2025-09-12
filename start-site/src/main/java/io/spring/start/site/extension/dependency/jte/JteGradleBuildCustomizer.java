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
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to configure the JTE Gradle plugin.
 *
 * @author Moritz Halbritter
 */
class JteGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	@Override
	public void customize(GradleBuild build) {
		Dependency jteStarter = build.dependencies().get("jte");
		build.plugins().add("gg.jte.gradle", (plugin) -> plugin.setVersion(jteStarter.getVersion().getValue()));
		build.extensions().customize("jte", (jte) -> {
			jte.invoke("generate");
			jte.attribute("binaryStaticContent", "true");
		});
	}

}
