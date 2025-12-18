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
			3.5.0,			2.3.0
			4.0.0-M1,		2.3.0
			4.0.0-M2,		2.3.0
			4.0.0-M3,		2.3.0
			4.0.0-RC1,		3.0.1
			4.0.0-SNAPSHOT,	3.0.1
			4.0.0,			3.0.1
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
