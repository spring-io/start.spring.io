/*
 * Copyright 2012-2021 the original author or authors.
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
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link HelpDocumentCustomizer} that provides additional references when Wavefront is
 * selected.
 *
 * @author Stephane Nicoll
 */
class WavefrontHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final Build build;

	WavefrontHelpDocumentCustomizer(Build build) {
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("## Observability with Wavefront%n%n"));
		sb.append(String.format(
				"If you don't have a Wavefront account, the starter will create a freemium account for you.%n"));
		sb.append(String.format("The URL to access the Wavefront Service dashboard is logged on startup.%n"));

		if (this.build.dependencies().has("web") || this.build.dependencies().has("webflux")) {
			sb.append(
					String.format("%nYou can also access your dashboard using the `/actuator/wavefront` endpoint.%n"));
		}

		if (!this.build.dependencies().has("cloud-starter-sleuth")) {
			sb.append(String.format(
					"%nFinally, you can opt-in for distributed tracing by adding the Spring Cloud Sleuth starter.%n"));
		}
		document.addSection((writer) -> writer.print(sb));
	}

}
