/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.description;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionDiff;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for customizations relevant to the
 * {@link ProjectDescription}.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
public class DescriptionProjectGenerationConfiguration {

	@Bean
	public InvalidPackageNameHelpDocumentCustomizer invalidPackageNameHelpDocumentCustomizer(
			ProjectDescriptionDiff diff, ProjectDescription description) {
		return new InvalidPackageNameHelpDocumentCustomizer(diff, description);
	}

}
