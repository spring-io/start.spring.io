/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradlePlugin;
import io.spring.initializr.generator.buildsystem.gradle.StandardGradlePlugin;
import io.spring.initializr.generator.version.VersionReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Shared tests for {@link GraalVmGradleBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class GraalVmGradleBuildCustomizerTests {

	@Test
	void gradleBuildConfigureNativeBuildToolsPlugin() {
		GraalVmGradleBuildCustomizer customizer = new GraalVmGradleBuildCustomizer("1.0.0");
		GradleBuild build = createBuild();
		customizer.customize(build);
		GradlePlugin nbtPlugin = build.plugins()
			.values()
			.filter((plugin) -> plugin.getId().equals("org.graalvm.buildtools.native"))
			.findAny()
			.orElse(null);
		assertThat(nbtPlugin).isNotNull();
		assertThat(nbtPlugin).isInstanceOf(StandardGradlePlugin.class)
			.satisfies((plugin) -> assertThat(((StandardGradlePlugin) plugin).getVersion()).isEqualTo("1.0.0"));
	}

	protected GradleBuild createBuild() {
		GradleBuild build = new GradleBuild();
		build.dependencies()
			.add("native",
					Dependency.withCoordinates("com.example", "native")
						.version(VersionReference.ofProperty("native.version"))
						.build());
		return build;
	}

}
