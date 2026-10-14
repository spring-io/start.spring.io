/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.description;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link InvalidPackageNameHelpDocumentCustomizer}.
 *
 * @author Stephane Nicoll
 */
class InvalidPackageNameHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void warningAddedWithInvalidPackageName() {
		assertHelpDocument("com.my-invalid-package").lines()
			.containsSubsequence("# Read Me First",
					"* The original package name 'com.my-invalid-package' is invalid and this project uses 'com.my_invalid_package' instead.");
	}

	@Test
	void warningNotAddedWithValidPackageName() {
		assertHelpDocument("com.example.valid").doesNotContain("# Read Me First");
	}

	private TextAssert assertHelpDocument(String packageName) {
		ProjectRequest request = createProjectRequest("web");
		request.setPackageName(packageName);
		return assertThat(helpDocument(request));
	}

}
