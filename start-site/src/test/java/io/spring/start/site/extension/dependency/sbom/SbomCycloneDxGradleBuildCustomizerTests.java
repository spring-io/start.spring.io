/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.sbom;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradlePlugin;
import io.spring.initializr.generator.buildsystem.gradle.StandardGradlePlugin;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.version.Version;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SbomCycloneDxGradleBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class SbomCycloneDxGradleBuildCustomizerTests {

	private SbomCycloneDxGradleBuildCustomizer customizer;

	private MutableProjectDescription description;

	@BeforeEach
	void setUp() {
		this.description = new MutableProjectDescription();
		this.customizer = new SbomCycloneDxGradleBuildCustomizer(this.description);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			4.0.0,			3.5.1
			4.1.0,			3.5.1
			4.1.1-SNAPSHOT,	3.5.1
			""")
	void shouldUseCorrectPluginVersion(String bootVersion, String pluginVersion) {
		GradleBuild gradleBuild = gradleBuildFor(bootVersion);
		assertThat(gradleBuild.plugins().values()).anySatisfy(sbomPlugin(pluginVersion));
	}

	private GradleBuild gradleBuildFor(String bootVersion) {
		this.description.setPlatformVersion(Version.parse(bootVersion));
		GradleBuild gradleBuild = new GradleBuild();
		this.customizer.customize(gradleBuild);
		return gradleBuild;
	}

	private ThrowingConsumer<GradlePlugin> sbomPlugin(String version) {
		return (plugin) -> {
			assertThat(plugin.getId()).isEqualTo("org.cyclonedx.bom");
			assertThat(plugin).isInstanceOf(StandardGradlePlugin.class);
			assertThat(((StandardGradlePlugin) plugin).getVersion()).isEqualTo(version);
		};
	}

}
