/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.springcloud;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.io.template.TemplateRenderer;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.metadata.DependenciesCapability;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * A {@link HelpDocumentCustomizer} that adds a warning when user has requested Spring
 * Cloud Netflix dependencies that are in maintenance mode.
 *
 * @author Olga Maciaszek-Sharma
 * @author Stephane Nicoll
 */
class SpringCloudNetflixMaintenanceModeHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final Set<String> maintenanceModuleIds = new HashSet<>(Arrays.asList("cloud-ribbon", "cloud-hystrix",
			"cloud-hystrix-dashboard", "cloud-turbine", "cloud-turbine-stream", "cloud-zuul"));

	private final InitializrMetadata metadata;

	private final Build build;

	private final TemplateRenderer templateRenderer;

	SpringCloudNetflixMaintenanceModeHelpDocumentCustomizer(InitializrMetadata metadata, Build build,
			TemplateRenderer templateRenderer) {
		this.metadata = metadata;
		this.build = build;
		this.templateRenderer = templateRenderer;
	}

	@Override
	public void customize(HelpDocument helpDocument) {
		DependenciesCapability availableDependencies = this.metadata.getDependencies();
		Set<Dependency> maintenanceModeDependencies = this.build.dependencies().ids()
				.filter(maintenanceModuleIds::contains).map(availableDependencies::get).filter(Objects::nonNull)
				.collect(Collectors.toSet());
		if (!maintenanceModeDependencies.isEmpty()) {
			helpDocument.addSection(
					new SpringCloudNetflixMaintenanceModeSection(maintenanceModeDependencies, this.templateRenderer));
		}
	}

}
