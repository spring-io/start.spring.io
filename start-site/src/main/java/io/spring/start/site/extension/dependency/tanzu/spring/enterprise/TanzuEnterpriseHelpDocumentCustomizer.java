/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.tanzu.spring.enterprise;

import java.util.Collections;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.io.text.MustacheSection;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildMetadataResolver;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * {@link HelpDocumentCustomizer} to add a VMware Tanzu Spring Enterprise section to the
 * help file if the project has the {@code tanzu-spring-enterprise} facet.
 *
 * @author Moritz Halbritter
 */
class TanzuEnterpriseHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final BuildMetadataResolver buildResolver;

	private final MustacheTemplateRenderer templateRenderer;

	private final Build build;

	TanzuEnterpriseHelpDocumentCustomizer(InitializrMetadata metadata, ProjectDescription description,
			MustacheTemplateRenderer templateRenderer, Build build) {
		this.buildResolver = new BuildMetadataResolver(metadata, description.getPlatformVersion());
		this.build = build;
		this.templateRenderer = templateRenderer;
	}

	@Override
	public void customize(HelpDocument document) {
		if (!hasFacet()) {
			return;
		}
		document
			.addSection(new MustacheSection(this.templateRenderer, "tanzu-spring-enterprise", Collections.emptyMap()));
	}

	private boolean hasFacet() {
		return this.buildResolver.hasFacet(this.build, "tanzu-spring-enterprise");
	}

}
