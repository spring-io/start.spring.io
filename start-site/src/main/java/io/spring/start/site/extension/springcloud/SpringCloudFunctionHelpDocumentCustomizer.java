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
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link HelpDocumentCustomizer} that adds information regarding build file setup for
 * running Spring Cloud Function projects on various cloud platforms.
 *
 * @author Olga Maciaszek-Sharma
 */
class SpringCloudFunctionHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final Log LOG = LogFactory.getLog(SpringCloudFunctionHelpDocumentCustomizer.class);

	private static final String SNAPSHOT = "SNAPSHOT";

	private static final String MISSING_TEMPLATE_SUFFIX = "missing";

	private static final String SPRING_CLOUD_FUNCTION_DEPENDENCY_ID = "cloud-function";

	private static final String TEMPLATE_PREFIX = "spring-cloud-function-build-setup-";

	private static final String SPRING_CLOUD_FUNCTION_ARTIFACT_ID = "org.springframework.cloud:spring-cloud-function-core";

	private final Set<String> buildDependencies;

	private final MustacheTemplateRenderer templateRenderer;

	private final SpringCloudProjectVersionResolver projectVersionResolver;

	private final ResolvedProjectDescription description;

	SpringCloudFunctionHelpDocumentCustomizer(Build build, ResolvedProjectDescription description,
			MustacheTemplateRenderer templateRenderer, SpringCloudProjectVersionResolver projectVersionResolver) {
		this.buildDependencies = build.dependencies().ids().collect(Collectors.toSet());
		this.description = description;
		this.templateRenderer = templateRenderer;
		this.projectVersionResolver = projectVersionResolver;
	}

	@Override
	public void customize(HelpDocument helpDocument) {
		this.buildDependencies.stream()
				.filter((dependencyId) -> dependencyId.equals(SPRING_CLOUD_FUNCTION_DEPENDENCY_ID)).findAny()
				.ifPresent((dependency) -> addBuildSetupInfo(helpDocument));
	}

	private void addBuildSetupInfo(HelpDocument helpDocument) {
		Version bootVersion = this.description.getPlatformVersion();
		String springCloudFunctionVersion = this.projectVersionResolver.resolveVersion(bootVersion,
				SPRING_CLOUD_FUNCTION_ARTIFACT_ID);
		if (springCloudFunctionVersion == null) {
			LOG.warn("Spring Cloud Function version could not be resolved for Spring Boot version: "
					+ bootVersion.toString());
			return;
		}
		if (isSnapshot(springCloudFunctionVersion)) {
			LOG.debug("Spring Cloud Function version " + springCloudFunctionVersion
					+ " is a snapshot. No documents are present for this version to link to.");
			return;
		}
		String buildSystemId = this.description.getBuildSystem().id();
		Set<CloudPlatform> presentCloudPlatforms = cloudPlatformsFromDependencies();

		Map<Boolean, List<CloudPlatform>> platformsByBuildSystemSupport = presentCloudPlatforms.stream()
				.collect(Collectors.partitioningBy(
						(cloudPlatform) -> cloudPlatform.getPluginsForBuildSystems().contains(buildSystemId)));
		platformsByBuildSystemSupport.get(true)
				.forEach((cloudPlatform) -> helpDocument.addSection(springCloudFunctionBuildSetupSection(cloudPlatform,
						buildSystemId, springCloudFunctionVersion, getTemplateName(cloudPlatform))));
		platformsByBuildSystemSupport.get(false)
				.forEach((cloudPlatform) -> helpDocument.addSection(springCloudFunctionBuildSetupSection(cloudPlatform,
						buildSystemId, springCloudFunctionVersion, TEMPLATE_PREFIX + MISSING_TEMPLATE_SUFFIX)));
	}

	private boolean isSnapshot(String springCloudFunctionVersion) {
		return springCloudFunctionVersion.toUpperCase().contains(SNAPSHOT);
	}

	private Set<CloudPlatform> cloudPlatformsFromDependencies() {
		return new HashSet<>(Arrays.stream(CloudPlatform.values())
				.collect(Collectors.partitioningBy(
						(cloudPlatform) -> this.buildDependencies.contains(cloudPlatform.getDependencyId())))
				.get(true));
	}

	private SpringCloudFunctionBuildSetupSection springCloudFunctionBuildSetupSection(CloudPlatform cloudPlatform,
			String buildSystemId, String version, String templateName) {
		return new SpringCloudFunctionBuildSetupSection(
				new SpringCloudFunctionBuildSetupSection.Data(cloudPlatform, buildSystemId.toUpperCase(), version),
				this.templateRenderer, templateName);
	}

	private String getTemplateName(CloudPlatform cloudPlatform) {
		return TEMPLATE_PREFIX + cloudPlatform.toString().toLowerCase();
	}

	/**
	 * Represents Cloud Platforms that provide build plugins that can be used for
	 * deploying Spring Cloud Function applications.
	 */
	enum CloudPlatform {

		AWS("AWS", "cloud-aws"), AZURE("Azure", "azure-support", Collections.singletonList(MavenBuildSystem.ID));

		private final String name;

		private final String dependencyId;

		private List<String> pluginsForBuildSystems = Arrays.asList(MavenBuildSystem.ID, GradleBuildSystem.ID);

		CloudPlatform(String name, String dependencyId, List<String> pluginsForBuildSystems) {
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
			return this.dependencyId;
		}

		List<String> getPluginsForBuildSystems() {
			return Collections.unmodifiableList(this.pluginsForBuildSystems);
		}

	}

}
