/*
 * Copyright 2012-2021 the original author or authors.
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

package io.spring.start.site.extension.dependency.springintegration;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that adds a reference link for each supported
 * Spring Integration entry when Spring Integration is selected.
 *
 * @author Artem Bilan
 * @author Stephane Nicoll
 */
class SpringIntegrationHelpCustomizer implements HelpDocumentCustomizer {

	private final SpringIntegrationModuleRegistry modulesRegistry;

	private final Build build;

	SpringIntegrationHelpCustomizer(SpringIntegrationModuleRegistry modulesRegistry, Build build) {
		this.modulesRegistry = modulesRegistry;
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		this.modulesRegistry.modules()
				.filter((module) -> module.match(this.build))
				.forEach((module) ->
						document.gettingStarted()
								.addReferenceDocLink(module.getDocumentationUrl(),
										String.format("Spring Integration %s Reference Guide", module.getName())));
	}

}
