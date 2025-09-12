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

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Adds {@code org.springframework.ai:spring-ai-advisors-vector-store} if any model and
 * any vector store is selected.
 *
 * @author Moritz Halbritter
 */
class SpringAiAdvisorsVectorStoreBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (hasModel(build) && hasVectorStore(build)) {
			Dependency advisorsVectorStore = Dependency
				.withCoordinates("org.springframework.ai", "spring-ai-advisors-vector-store")
				.build();
			build.dependencies().add("spring-ai-advisors-vector-store", advisorsVectorStore);
		}
	}

	private boolean hasVectorStore(Build build) {
		return build.dependencies()
			.items()
			.anyMatch((dependency) -> dependency.getGroupId().equals("org.springframework.ai")
					&& dependency.getArtifactId().startsWith("spring-ai-starter-vector-store-"));
	}

	private boolean hasModel(Build build) {
		return build.dependencies()
			.items()
			.anyMatch((dependency) -> dependency.getGroupId().equals("org.springframework.ai")
					&& dependency.getArtifactId().startsWith("spring-ai-starter-model-"));
	}

}
