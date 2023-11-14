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

import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link DgsCodegenHelpDocumentCustomizer}.
 *
 * @author Brian Clozel
 */
class DgsCodegenHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Autowired
	private MustacheTemplateRenderer templateRenderer;

	@Test
	void mavenBuildAddsLinkToMavenCodegenPlugin() {

		assertHelpDocument("maven-project", "dgs-codegen").contains("## GraphQL code generation with DGS")
			.contains("[plugin configuration options](https://github.com/deweyjose/graphqlcodegen)");
	}

	@Test
	void gradleBuildAddsLinkToGradleCodegenPlugin() {
		assertHelpDocument("gradle-project", "dgs-codegen").contains("## GraphQL code generation with DGS")
			.contains(
					"[plugin configuration options](https://netflix.github.io/dgs/generating-code-from-schema/#configuring-code-generation)");
	}

	private TextAssert assertHelpDocument(String type, String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setType(type);
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md"));
	}

}
