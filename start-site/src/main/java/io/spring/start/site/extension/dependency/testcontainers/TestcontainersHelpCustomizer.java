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

package io.spring.start.site.extension.dependency.testcontainers;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that adds a reference link for each supported
 * Testcontainers entry when Testcontainers is selected.
 *
 * @author Maciej Walkowiak
 * @author Stephane Nicoll
 */
class TestcontainersHelpCustomizer implements HelpDocumentCustomizer {

	private final TestContainerModuleRegistry modulesRegistry;

	private final Build build;

	TestcontainersHelpCustomizer(TestContainerModuleRegistry modulesRegistry, Build build) {
		this.modulesRegistry = modulesRegistry;
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		this.modulesRegistry.modules().forEach((module) -> {
			if (module.match(this.build)) {
				document.gettingStarted().addReferenceDocLink(module.getDocumentationUrl(),
						String.format("Testcontainers %s Reference Guide", module.getName()));
			}
		});
	}

}
