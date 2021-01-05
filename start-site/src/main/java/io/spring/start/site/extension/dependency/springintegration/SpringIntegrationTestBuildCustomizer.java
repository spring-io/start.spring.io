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

package io.spring.start.site.extension.dependency.springintegration;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;

/**
 * A {@link BuildCustomizer} that automatically adds {@code spring-integration-test} when
 * Spring Integration is selected.
 *
 * @author Stephane Nicoll
 * @author Artem Bilan
 */
class SpringIntegrationTestBuildCustomizer implements BuildCustomizer<Build>, HelpDocumentCustomizer {

	private final SpringIntegrationModule testModule =
			new SpringIntegrationModule("Test Module",
					"https://docs.spring.io/spring-integration/reference/html/testing.html",
					(build) -> build.dependencies()
							.add("spring-integration-test",
									Dependency.withCoordinates("org.springframework.integration",
											"spring-integration-test")
											.scope(DependencyScope.TEST_COMPILE)),
					"integration");

	@Override
	public void customize(Build build) {
		this.testModule.customize(build);
	}

	@Override public void customize(HelpDocument helpDocument) {
		helpDocument.gettingStarted().addReferenceDocLink(this.testModule.getDocumentationUrl(),
				String.format("Spring Integration %s Reference Guide", this.testModule.getName()));
	}

	@Override public int getOrder() {
		return 0;
	}

}
