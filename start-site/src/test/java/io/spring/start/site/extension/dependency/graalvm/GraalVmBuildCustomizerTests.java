/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.version.VersionReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stephane Nicoll
 */
public class GraalVmBuildCustomizerTests {

	@Test
	void gradleBuildRemoveNativeDependency() {
		GraalVmBuildCustomizer customizer = new GraalVmBuildCustomizer();
		GradleBuild build = new GradleBuild();
		build.dependencies()
			.add("native",
					Dependency.withCoordinates("com.example", "native")
						.version(VersionReference.ofProperty("native.version"))
						.build());
		customizer.customize(build);
		assertThat(build.dependencies().has("native")).isFalse();
	}

	@Test
	void mavenBuildRemoveNativeDependency() {
		GraalVmBuildCustomizer customizer = new GraalVmBuildCustomizer();
		MavenBuild build = new MavenBuild();
		build.dependencies()
			.add("native",
					Dependency.withCoordinates("com.example", "native")
						.version(VersionReference.ofProperty("native.version"))
						.build());
		customizer.customize(build);
		assertThat(build.dependencies().has("native")).isFalse();
	}

}
