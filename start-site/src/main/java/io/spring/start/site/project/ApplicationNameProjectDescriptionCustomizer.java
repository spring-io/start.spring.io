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

package io.spring.start.site.project;

import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.metadata.InitializrConfiguration;
import io.spring.initializr.web.project.MetadataProjectDescriptionCustomizer;

import org.springframework.util.StringUtils;

/**
 * {@link ProjectDescriptionCustomizer} that derives the application name from the
 * artifact ID when the name is empty. Runs before the
 * {@link MetadataProjectDescriptionCustomizer} so that a meaningful application name is
 * generated instead of the fallback.
 *
 * @author Moritz Halbritter
 */
class ApplicationNameProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private final InitializrConfiguration configuration;

	ApplicationNameProjectDescriptionCustomizer(InitializrConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void customize(MutableProjectDescription description) {
		if (!StringUtils.hasText(description.getApplicationName()) && !StringUtils.hasText(description.getName())
				&& StringUtils.hasText(description.getArtifactId())) {
			description.setApplicationName(this.configuration.generateApplicationName(description.getArtifactId()));
		}
	}

	@Override
	public int getOrder() {
		return -1;
	}

}
