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

import java.util.HashMap;
import java.util.Map;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.io.text.MustacheSection;
import io.spring.initializr.generator.io.text.Section;
import io.spring.initializr.generator.spring.documentation.PreDefinedSection;

/**
 * A {@link Section} that provides information about build file setup for running Spring
 * Cloud Function projects on various cloud platforms.
 *
 * @author Olga Maciaszek-Sharma
 */
class SpringCloudFunctionBuildSetupSection extends PreDefinedSection {

	private static final String templateName = "spring-cloud-function-build-setup";

	private static final String AWS_MAVEN_SETUP_EXAMPLE = "https://github.com/spring-cloud/spring-cloud-function/blob/master/spring-cloud-function-samples/function-sample-aws/pom.xml";

	private static final String AWS_GRADLE_SETUP_EXAMPLE = "https://github.com/spring-cloud/spring-cloud-function/blob/master/spring-cloud-function-samples/function-sample-aws/build.gradle";

	private static final String AZURE_MAVEN_SETUP_EXAMPLE = "https://github.com/spring-cloud/spring-cloud-function/blob/master/spring-cloud-function-samples/function-sample-azure/pom.xml";

	private static final String FUNCTION_BUILD_SETUP_INFO = "Additional build setup for Spring Cloud Function apps - ";

	SpringCloudFunctionBuildSetupSection(
			SpringCloudFunctionHelpDocumentCustomizer.CloudPlatform platform,
			String buildTool, MustacheTemplateRenderer templateRenderer) {
		super(FUNCTION_BUILD_SETUP_INFO + platform.getName());
		Map<String, Object> model = new HashMap<>();
		model.put("platform", platform.getName());
		model.put("buildTool", buildTool);
		model.put("link", getExampleLink(platform, buildTool));
		MustacheSection mustacheSection = new MustacheSection(templateRenderer,
				templateName, model);
		addSection(mustacheSection);
	}

	private String getExampleLink(
			SpringCloudFunctionHelpDocumentCustomizer.CloudPlatform platform,
			String buildTool) {
		if (SpringCloudFunctionHelpDocumentCustomizer.CloudPlatform.isAws(platform)
				&& MavenBuildSystem.ID.toUpperCase().equals(buildTool)) {
			return AWS_MAVEN_SETUP_EXAMPLE;
		}
		if (SpringCloudFunctionHelpDocumentCustomizer.CloudPlatform.isAws(platform)
				&& GradleBuildSystem.ID.toUpperCase().equals(buildTool)) {
			return AWS_GRADLE_SETUP_EXAMPLE;
		}
		else {
			return AZURE_MAVEN_SETUP_EXAMPLE;
		}
	}

}
