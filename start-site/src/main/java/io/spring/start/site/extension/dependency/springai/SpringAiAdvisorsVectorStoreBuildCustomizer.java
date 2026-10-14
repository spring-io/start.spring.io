/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Adds {@code org.springframework.ai:spring-ai-vector-store-advisor} or
 * {@code org.springframework.ai:spring-ai-advisors-vector-store} if any model and any
 * vector store is selected.
 *
 * @author Moritz Halbritter
 */
class SpringAiAdvisorsVectorStoreBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (hasModel(build) && hasVectorStore(build)) {
			Dependency dependency;
			dependency = Dependency.withCoordinates("org.springframework.ai", "spring-ai-vector-store-advisor").build();
			build.dependencies().add("spring-ai-vector-store-advisor", dependency);
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
