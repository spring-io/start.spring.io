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
