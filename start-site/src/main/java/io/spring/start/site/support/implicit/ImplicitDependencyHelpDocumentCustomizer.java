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

package io.spring.start.site.support.implicit;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that customize the help document if necessary based on
 * {@link ImplicitDependency implicit dependencies}.
 *
 * @author Stephane Nicoll
 */
public class ImplicitDependencyHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final Iterable<ImplicitDependency> dependencies;

	private final Build build;

	public ImplicitDependencyHelpDocumentCustomizer(Iterable<ImplicitDependency> dependencies, Build build) {
		this.dependencies = dependencies;
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		for (ImplicitDependency dependency : this.dependencies) {
			dependency.customize(document, this.build);
		}
	}

}
