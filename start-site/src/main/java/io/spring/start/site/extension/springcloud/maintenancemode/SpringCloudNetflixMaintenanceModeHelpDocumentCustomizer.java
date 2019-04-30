/*
 * Copyright 2019-2019 the original author or authors.
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

package io.spring.start.site.extension.springcloud.maintenancemode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import io.spring.initializr.generator.io.template.TemplateRenderer;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
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
 */
public class SpringCloudNetflixMaintenanceModeHelpDocumentCustomizer
		implements HelpDocumentCustomizer {

	private static final Set<String> maintenanceModuleIds = new HashSet<>(
			Arrays.asList("cloud-ribbon", "cloud-hystrix", "cloud-hystrix-dashboard",
					"cloud-turbine", "cloud-turbine-stream", "cloud-zuul"));

	private final InitializrMetadata initializrMetadata;

	private final ResolvedProjectDescription resolvedProjectDescription;

	private final TemplateRenderer templateRenderer;

	public SpringCloudNetflixMaintenanceModeHelpDocumentCustomizer(
			InitializrMetadata initializrMetadata,
			ResolvedProjectDescription resolvedProjectDescription,
			TemplateRenderer templateRenderer) {
		this.initializrMetadata = initializrMetadata;
		this.resolvedProjectDescription = resolvedProjectDescription;
		this.templateRenderer = templateRenderer;
	}

	@Override
	public void customize(HelpDocument helpDocument) {
		DependenciesCapability availableDependencies = this.initializrMetadata
				.getDependencies();
		Set<Dependency> maintenanceModeDependencies = this.resolvedProjectDescription
				.getRequestedDependencies().keySet().stream()
				.filter(maintenanceModuleIds::contains).map(availableDependencies::get)
				.collect(Collectors.toSet());
		maintenanceModeDependencies.stream().findAny()
				.ifPresent((dependency) -> helpDocument
						.addSection(new SpringCloudNetflixMaintenanceModeWarningSection(
								maintenanceModeDependencies, this.templateRenderer)));
	}

}
