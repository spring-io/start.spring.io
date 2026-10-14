/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.lombok;

import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * Complete the setup for Lombok with Maven by adding Lombok as a dependency. It also adds
 * Lombok as a test annotation processor.
 *
 * @author Moritz Halbritter
 */
public class LombokMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private final InitializrMetadata metadata;

	public LombokMavenBuildCustomizer(InitializrMetadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public void customize(MavenBuild build) {
		Dependency lombok = this.metadata.getDependencies().get("lombok");
		build.dependencies()
			.add("lombok-compileOnly", lombok.getGroupId(), lombok.getArtifactId(), DependencyScope.COMPILE_ONLY);
		build.dependencies()
			.add("lombok-testAnnotationProcessor", lombok.getGroupId(), lombok.getArtifactId(),
					DependencyScope.TEST_ANNOTATION_PROCESSOR);
	}

}
