/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.version.VersionReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DgsCodegenBuildCustomizer}.
 *
 * @author Brian Clozel
 */
public class DgsCodegenBuildCustomizerTests {

	@Test
	void gradleBuildShouldRemoveCodegenDependency() {
		DgsCodegenBuildCustomizer customizer = new DgsCodegenBuildCustomizer();
		GradleBuild build = new GradleBuild();
		build.dependencies()
			.add("dgs-codegen",
					Dependency.withCoordinates("com.example", "dgs-codegen")
						.version(VersionReference.ofProperty("dgs-codegen.version"))
						.build());
		customizer.customize(build);
		assertThat(build.dependencies().has("dgs-codegen")).isFalse();
	}

	@Test
	void mavenBuildShouldRemoveCodegenDependency() {
		DgsCodegenBuildCustomizer customizer = new DgsCodegenBuildCustomizer();
		MavenBuild build = new MavenBuild();
		build.dependencies()
			.add("dgs-codegen",
					Dependency.withCoordinates("com.example", "dgs-codegen")
						.version(VersionReference.ofProperty("dgs-codegen.version"))
						.build());
		customizer.customize(build);
		assertThat(build.dependencies().has("dgs-codegen")).isFalse();
	}

}
