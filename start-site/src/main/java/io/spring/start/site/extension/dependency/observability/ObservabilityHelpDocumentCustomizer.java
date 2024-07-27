/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * {@link HelpDocumentCustomizer} implementation for Observability.
 *
 * @author Stephane Nicoll
 */
public class ObservabilityHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final VersionRange SPRING_BOOT_3_3_0_OR_LATER = VersionParser.DEFAULT.parseRange("3.3.0");

	private final Version platformVersion;

	private final Build build;

	public ObservabilityHelpDocumentCustomizer(ProjectDescription description, Build build) {
		this.platformVersion = description.getPlatformVersion();
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		if (this.build.dependencies().has("distributed-tracing")) {
			document.gettingStarted()
				.addReferenceDocLink("https://docs.micrometer.io/tracing/reference/index.html",
						"Distributed Tracing Reference Guide");
			document.gettingStarted()
				.addReferenceDocLink(String.format(shouldChangeUrl()
						? "https://docs.spring.io/spring-boot/%s/reference/actuator/tracing.html"
						: "https://docs.spring.io/spring-boot/docs/%s/reference/html/actuator.html#actuator.micrometer-tracing.getting-started",
						this.platformVersion), "Getting Started with Distributed Tracing");

		}
	}

	private boolean shouldChangeUrl() {

		return this.SPRING_BOOT_3_3_0_OR_LATER.match(this.platformVersion);
	}

}
