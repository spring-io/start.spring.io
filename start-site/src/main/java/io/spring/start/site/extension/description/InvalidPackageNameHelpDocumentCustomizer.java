/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.description;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionDiff;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that adds a warning when the package name was changed.
 *
 * @author Stephane Nicoll
 */
public class InvalidPackageNameHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final ProjectDescriptionDiff diff;

	private final ProjectDescription description;

	public InvalidPackageNameHelpDocumentCustomizer(ProjectDescriptionDiff diff, ProjectDescription description) {
		this.diff = diff;
		this.description = description;
	}

	@Override
	public void customize(HelpDocument document) {
		this.diff.ifPackageNameChanged(this.description,
				(original, current) -> document.getWarnings()
					.addItem(String.format(
							"The original package name '%s' is invalid and this project uses '%s' instead.", original,
							current)));

	}

}
