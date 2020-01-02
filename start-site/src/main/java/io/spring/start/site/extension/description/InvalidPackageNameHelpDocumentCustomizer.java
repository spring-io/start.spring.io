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
								"The original package name '%s' is invalid and this project uses '%s' instead.",
								original, current)));

	}

}
