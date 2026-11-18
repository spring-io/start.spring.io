/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
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
