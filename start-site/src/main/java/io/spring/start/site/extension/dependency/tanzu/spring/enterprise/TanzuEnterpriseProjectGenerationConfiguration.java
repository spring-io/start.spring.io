/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.tanzu.spring.enterprise;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration Project generation configuration} for VMware
 * Tanzu Spring Enterprise extensions.
 *
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
class TanzuEnterpriseProjectGenerationConfiguration {

	@Bean
	TanzuEnterpriseHelpDocumentCustomizer tanzuEnterpriseHelpDocumentCustomizer(InitializrMetadata metadata,
			ProjectDescription description, MustacheTemplateRenderer templateRenderer, Build build) {
		return new TanzuEnterpriseHelpDocumentCustomizer(metadata, description, templateRenderer, build);
	}

	@Bean
	TanzuEnterpriseScgExtensionsBuildCustomizer tanzuEnterpriseScgExtensionsBuildCustomizer() {
		return new TanzuEnterpriseScgExtensionsBuildCustomizer();
	}

}
