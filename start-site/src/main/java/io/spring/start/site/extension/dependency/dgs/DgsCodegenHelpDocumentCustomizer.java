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

import java.util.HashMap;
import java.util.Map;

import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * Provide additional information when DGS Codegen Support is selected.
 *
 * @author Brian Clozel
 */
class DgsCodegenHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final BuildSystem buildSystem;

	DgsCodegenHelpDocumentCustomizer(ProjectDescription description) {
		this.buildSystem = description.getBuildSystem();
	}

	@Override
	public void customize(HelpDocument document) {
		Map<String, Object> model = new HashMap<>();

		if (MavenBuildSystem.ID.equals(this.buildSystem.id())) {
			model.put("dsgCodegenOptionsLink", "https://github.com/deweyjose/graphqlcodegen");
		}
		else if (GradleBuildSystem.ID.equals(this.buildSystem.id())) {
			model.put("dsgCodegenOptionsLink",
					"https://netflix.github.io/dgs/generating-code-from-schema/#configuring-code-generation");
		}
		document.addSection("dgscodegen", model);
	}

}
