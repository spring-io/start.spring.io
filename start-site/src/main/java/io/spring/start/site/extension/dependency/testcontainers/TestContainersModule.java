/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.start.site.extension.dependency.testcontainers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.documentation.HelpDocument;

/**
 * Represent a Testcontainers module and how it should affect a generated project that
 * matches a configurable set of entries.
 *
 * @author Stephane Nicoll
 */
class TestContainersModule {

	private final List<String> triggeredDependencyIds;

	private final Consumer<Build> buildCustomizer;

	private final Consumer<HelpDocument> helpDocumentCustomizer;

	TestContainersModule(String triggeredDependencyId, Consumer<Build> buildCustomizer,
			Consumer<HelpDocument> helpDocumentCustomizer) {
		this(Collections.singleton(triggeredDependencyId), buildCustomizer, helpDocumentCustomizer);
	}

	TestContainersModule(Collection<String> triggeredDependencyId, Consumer<Build> buildCustomizer,
			Consumer<HelpDocument> helpDocumentCustomizer) {
		this.triggeredDependencyIds = new ArrayList<>(triggeredDependencyId);
		this.buildCustomizer = buildCustomizer;
		this.helpDocumentCustomizer = helpDocumentCustomizer;
	}

	void customize(Build build) {
		if (hasMatchingDependency(build)) {
			this.buildCustomizer.accept(build);
		}
	}

	void customizeHelpDocument(Build build, HelpDocument document) {
		if (hasMatchingDependency(build)) {
			this.helpDocumentCustomizer.accept(document);
		}

	}

	boolean hasMatchingDependency(Build build) {
		for (String triggeredDependencyId : this.triggeredDependencyIds) {
			if (build.dependencies().has(triggeredDependencyId)) {
				return true;
			}
		}
		return false;
	}

}
