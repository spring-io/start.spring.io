/*
 * Copyright 2012 - present the original author or authors.
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
