/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.project.ProjectDescription;

/**
 * {@link SpringCloudContractGradleBuildCustomizer} implementation for the Groovy DSL.
 *
 * @author Stephane Nicoll
 */
class SpringCloudContractGroovyDslGradleBuildCustomizer extends SpringCloudContractGradleBuildCustomizer {

	SpringCloudContractGroovyDslGradleBuildCustomizer(ProjectDescription description,
			SpringCloudProjectVersionResolver projectsVersionResolver) {
		super(description, projectsVersionResolver);
	}

	@Override
	protected void configureContractsDsl(GradleBuild build) {
		build.snippets().add((indentingWriter) -> {
			indentingWriter.println("contracts {");
			indentingWriter.indented(() -> {
				if (build.dependencies().has("webflux")) {
					indentingWriter.println("testMode = 'WebTestClient'");
				}
			});
			indentingWriter.println("}");
		});
	}

}
