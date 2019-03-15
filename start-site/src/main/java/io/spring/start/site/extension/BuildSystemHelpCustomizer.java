/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that automatically adds reference links based on the
 * build system.
 *
 * @author Jenn Strater
 */
public class BuildSystemHelpCustomizer implements HelpDocumentCustomizer {

	private final ResolvedProjectDescription description;

	public BuildSystemHelpCustomizer(ResolvedProjectDescription description) {
		this.description = description;
	}

	@Override
	public void customize(HelpDocument helpDocument) {
		boolean isGradle = this.description.getBuildSystem() instanceof GradleBuild;
		boolean isMaven = this.description.getBuildSystem() instanceof MavenBuild;

		if (isGradle) {
			helpDocument.gettingStarted().addAdditionalLink(
					"https://scans.gradle.com#gradle",
					"free, shareable build insights for Gradle and Maven builds");
			helpDocument.gettingStarted().addReferenceDocLink("https://gradle.org",
					"Official Gradle documentation");
		}

		if (isMaven) {
			helpDocument.gettingStarted().addAdditionalLink(
					"https://scans.gradle.com#maven",
					"free, shareable build insights for Gradle and Maven builds");
			helpDocument.gettingStarted().addReferenceDocLink(
					"https://maven.apache.org/guides/index.html",
					"Official Apache Maven documentation");
		}
	}

}
