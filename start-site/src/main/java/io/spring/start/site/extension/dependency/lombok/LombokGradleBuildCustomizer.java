/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.lombok;

import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * Complete the setup for Lombok with Gradle by adding Lombok with {@code compileOnly}
 * scope. It also adds Lombok as a test annotation processor and as a dependency with
 * {@code testCompileOnly} scope as well.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
public class LombokGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final InitializrMetadata metadata;

	public LombokGradleBuildCustomizer(InitializrMetadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public void customize(GradleBuild build) {
		Dependency lombok = this.metadata.getDependencies().get("lombok");
		build.dependencies()
			.add("lombok-compileOnly", lombok.getGroupId(), lombok.getArtifactId(), DependencyScope.COMPILE_ONLY);
		build.dependencies()
			.add("lombok-testAnnotationProcessor", lombok.getGroupId(), lombok.getArtifactId(),
					DependencyScope.TEST_ANNOTATION_PROCESSOR);
		build.dependencies()
			.add("lombok-testCompileOnly", lombok.getGroupId(), lombok.getArtifactId(),
					DependencyScope.TEST_COMPILE_ONLY);
	}

}
