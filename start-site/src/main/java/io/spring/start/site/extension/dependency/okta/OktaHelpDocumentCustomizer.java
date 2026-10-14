/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.okta;

import java.util.Collections;

import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.io.text.MustacheSection;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that provides some additional getting started
 * instructions for Okta.
 *
 * @author Stephane Nicoll
 */
public class OktaHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final MustacheTemplateRenderer templateRenderer;

	public OktaHelpDocumentCustomizer(MustacheTemplateRenderer templateRenderer) {
		this.templateRenderer = templateRenderer;
	}

	@Override
	public void customize(HelpDocument document) {
		document.addSection(new MustacheSection(this.templateRenderer, "okta", Collections.emptyMap()));
	}

}
