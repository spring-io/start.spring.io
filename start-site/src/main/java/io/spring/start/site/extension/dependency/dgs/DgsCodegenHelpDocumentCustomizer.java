/*
 * Copyright 2012 - present the original author or authors.
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
