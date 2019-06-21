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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that adds information regarding build file setup for
 * running Spring Cloud Function projects on various cloud platforms.
 *
 * @author Olga Maciaszek-Sharma
 */
class SpringCloudFunctionHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final String CLOUD_FUNCTION_DEPENDENCY_ID = "cloud-function";

	private static final String TEMPLATE_PREFIX = "spring-cloud-function-build-setup-";

	private final Set<String> buildDependencies;

	private final BuildSystem buildSystem;

	private final MustacheTemplateRenderer templateRenderer;

	SpringCloudFunctionHelpDocumentCustomizer(Build build,
			ResolvedProjectDescription resolvedProjectDescription,
			MustacheTemplateRenderer templateRenderer) {
		this.buildDependencies = build.dependencies().ids().collect(Collectors.toSet());
		this.buildSystem = resolvedProjectDescription.getBuildSystem();
		this.templateRenderer = templateRenderer;
	}

	@Override
	public void customize(HelpDocument helpDocument) {
		this.buildDependencies.stream().filter(
				(dependencyId) -> dependencyId.equals(CLOUD_FUNCTION_DEPENDENCY_ID))
				.findAny().ifPresent((dependency) -> addBuildInfo(helpDocument));
	}

	private void addBuildInfo(HelpDocument helpDocument) {
		Set<CloudPlatform> presentCloudPlatforms = cloudPlatformsFromDependencies();

		Map<Boolean, List<CloudPlatform>> platformsByBuildSystemSupport = presentCloudPlatforms
				.stream().collect(Collectors.partitioningBy(cloudPlatform -> cloudPlatform
						.getPluginsForBuildSystems().contains(this.buildSystem.id())));
		platformsByBuildSystemSupport.get(true)
				.forEach(cloudPlatform -> helpDocument
						.addSection(springCloudFunctionBuildSetupSection(cloudPlatform,
								getTemplateName(cloudPlatform))));

		platformsByBuildSystemSupport.get(false)
				.forEach(cloudPlatform -> helpDocument
						.addSection(springCloudFunctionBuildSetupSection(cloudPlatform,
								TEMPLATE_PREFIX + "missing")));
	}

	private Set<CloudPlatform> cloudPlatformsFromDependencies() {
		return new HashSet<>(Arrays.stream(CloudPlatform.values())
				.collect(Collectors.partitioningBy(cloudPlatform -> this.buildDependencies
						.contains(cloudPlatform.getDependencyId())))
				.get(true));
	}

	private SpringCloudFunctionBuildSetupSection springCloudFunctionBuildSetupSection(
			CloudPlatform cloudPlatform, String templateName) {
		return new SpringCloudFunctionBuildSetupSection(cloudPlatform,
				this.buildSystem.id().toUpperCase(), this.templateRenderer, templateName);
	}

	private String getTemplateName(CloudPlatform cloudPlatform) {
		return TEMPLATE_PREFIX + cloudPlatform.toString().toLowerCase();
	}

	/**
	 * Represents Cloud Platforms that provide build plugins that can be used for
	 * deploying Spring Cloud Function applications.
	 */
	enum CloudPlatform {

		AWS("AWS", "cloud-aws"), AZURE("Azure", "azure-support",
				Collections.singletonList(MavenBuildSystem.ID));

		private final String name;

		private final String dependencyId;

		private List<String> pluginsForBuildSystems = Arrays.asList(MavenBuildSystem.ID,
				GradleBuildSystem.ID);

		CloudPlatform(String name, String dependencyId,
				List<String> pluginsForBuildSystems) {
			this(name, dependencyId);
			this.pluginsForBuildSystems = pluginsForBuildSystems;

		}

		CloudPlatform(String name, String dependencyId) {
			this.name = name;
			this.dependencyId = dependencyId;
		}

		String getName() {
			return this.name;
		}

		String getDependencyId() {
			return dependencyId;
		}

		List<String> getPluginsForBuildSystems() {
			return Collections.unmodifiableList(pluginsForBuildSystems);
		}

	}

}