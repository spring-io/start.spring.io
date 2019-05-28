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

	private static final String FUNCTION_BUILD_SETUP_INFO = "Additional build setup for Spring Cloud Function apps - ";

	SpringCloudFunctionBuildSetupSection(Data sectionData, MustacheTemplateRenderer templateRenderer,
			String templateName) {
		super(FUNCTION_BUILD_SETUP_INFO + sectionData.platform.getName());
		Map<String, Object> model = new HashMap<>();
		model.put("platform", sectionData.platform.getName());
		model.put("buildTool", sectionData.buildTool);
		model.put("version", sectionData.springCloudFunctionVersion);
		MustacheSection mustacheSection = new MustacheSection(templateRenderer, templateName, model);
		addSection(mustacheSection);
	}

	/**
	 * Represents data necessary for generating
	 * {@link SpringCloudFunctionBuildSetupSection}.
	 */
	static class Data {

		private SpringCloudFunctionHelpDocumentCustomizer.CloudPlatform platform;

		private String buildTool;

		private String springCloudFunctionVersion;

		Data(SpringCloudFunctionHelpDocumentCustomizer.CloudPlatform platform, String buildTool,
				String springCloudFunctionVersion) {
			this.platform = platform;
			this.buildTool = buildTool;
			this.springCloudFunctionVersion = springCloudFunctionVersion;
		}

	}

}
