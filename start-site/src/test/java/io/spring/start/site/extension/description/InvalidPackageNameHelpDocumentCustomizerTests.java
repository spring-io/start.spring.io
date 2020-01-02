/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.start.site.extension.description;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link InvalidPackageNameHelpDocumentCustomizer}.
 *
 * @author Stephane Nicoll
 */
class InvalidPackageNameHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void warningAddedWithInvalidPackageName() {
		assertHelpDocument("com.my-invalid-package").lines().containsSubsequence("# Read Me First",
				"* The original package name 'com.my-invalid-package' is invalid and this project uses 'com.myinvalidpackage' instead.");
	}

	@Test
	void warningNotAddedWithValidPackageName() {
		assertHelpDocument("com.example.valid").doesNotContain("# Read Me First");
	}

	private TextAssert assertHelpDocument(String packageName) {
		ProjectRequest request = createProjectRequest("web");
		request.setPackageName(packageName);
		ProjectStructure project = generateProject(request);
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md"));
	}

}
