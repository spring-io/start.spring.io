/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.jte;

import java.util.Collections;

import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * {@link HelpDocumentCustomizer} to add a jte section.
 *
 * @author Moritz Halbritter
 */
class JteHelpDocumentCustomizer implements HelpDocumentCustomizer {

	@Override
	public void customize(HelpDocument document) {
		document.addSection("jte", Collections.emptyMap());
	}

}
