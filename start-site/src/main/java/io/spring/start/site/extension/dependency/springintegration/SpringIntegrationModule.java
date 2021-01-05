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

import java.util.function.Consumer;

import io.spring.initializr.generator.buildsystem.Build;

/**
 * Represent a Spring Integration module and how it should affect a generated project that
 * matches a configurable set of entries.
 *
 * @author Artem Bilan
 * @author Stephane Nicoll
 */
class SpringIntegrationModule {

	private final String name;

	private final String documentationUrl;

	private final String[] triggeredDependencyIds;

	private final Consumer<Build> buildCustomizer;

	SpringIntegrationModule(String name, String documentationUrl, Consumer<Build> buildCustomizer,
			String... triggeredDependencyIds) {

		this.name = name;
		this.documentationUrl = documentationUrl;
		this.triggeredDependencyIds = triggeredDependencyIds;
		this.buildCustomizer = buildCustomizer;
	}

	String getName() {
		return this.name;
	}

	String getDocumentationUrl() {
		return this.documentationUrl;
	}

	void customize(Build build) {
		if (match(build)) {
			this.buildCustomizer.accept(build);
		}
	}

	boolean match(Build build) {
		for (String triggeredDependencyId : this.triggeredDependencyIds) {
			if (build.dependencies().has(triggeredDependencyId)) {
				return true;
			}
		}
		return false;
	}

}
