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
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * {@link HelpDocumentCustomizer} implementation for Observability with Spring Boot 2.x.
 *
 * @author Stephane Nicoll
 */
public class Observability2xHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private final Build build;

	public Observability2xHelpDocumentCustomizer(Build build) {
		this.build = build;
	}

	@Override
	public void customize(HelpDocument document) {
		if (this.build.dependencies().has("distributed-tracing")) {
			document.gettingStarted()
				.addReferenceDocLink(
						"https://docs.spring.io/spring-cloud-sleuth/docs/current/reference/htmlsingle/spring-cloud-sleuth.html",
						"Spring Cloud Sleuth Reference Guide");
		}
	}

}
