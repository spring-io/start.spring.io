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

import java.util.Set;
import java.util.stream.Collectors;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.BuildSystem;
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
		this.buildDependencies.stream()
				.filter((dependencyId) -> dependencyId
						.equals(CLOUD_FUNCTION_DEPENDENCY_ID))
				.findAny()
				.ifPresent((dependency) -> addBuildInfoIfApplicable(helpDocument));
	}

	private void addBuildInfoIfApplicable(HelpDocument helpDocument) {
		if (this.buildDependencies.contains(CloudPlatform.AWS.dependencyId)) {
			helpDocument.addSection(
					new SpringCloudFunctionBuildSetupSection(CloudPlatform.AWS,
							this.buildSystem.id().toUpperCase(), this.templateRenderer));
		}
		if (this.buildDependencies.contains(CloudPlatform.AZURE.dependencyId)
				// No gradle plugin provided by Azure as of yet
				&& this.buildSystem instanceof MavenBuildSystem) {
			helpDocument.addSection(
					new SpringCloudFunctionBuildSetupSection(CloudPlatform.AZURE,
							this.buildSystem.id().toUpperCase(), this.templateRenderer));
		}
	}

	/**
	 * Contains Cloud Platform types for which additional build setup information can be
	 * added.
	 */
	enum CloudPlatform {

		AWS("AWS", "cloud-aws"), AZURE("Azure", "azure-support");

		private final String name;

		private final String dependencyId;

		CloudPlatform(String name, String dependencyId) {
			this.name = name;
			this.dependencyId = dependencyId;
		}

		static boolean isAws(
				SpringCloudFunctionHelpDocumentCustomizer.CloudPlatform platform) {
			return SpringCloudFunctionHelpDocumentCustomizer.CloudPlatform.AWS
					.equals(platform);
		}

		String getName() {
			return this.name;
		}

	}

}
