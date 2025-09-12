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

import java.util.Collections;
import java.util.Map;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradlePlugin;
import io.spring.initializr.generator.buildsystem.gradle.StandardGradlePlugin;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.start.site.SupportedBootVersion;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SbomCycloneDxGradleBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class SbomCycloneDxGradleBuildCustomizerTests {

	private SbomCycloneDxGradleBuildCustomizer customizer;

	private MutableProjectDescription description;

	private MavenVersionResolver versionResolver;

	@BeforeEach
	void setUp() {
		this.description = new MutableProjectDescription();
		this.versionResolver = mock(MavenVersionResolver.class);
		this.customizer = new SbomCycloneDxGradleBuildCustomizer(this.versionResolver, this.description);
	}

	@Test
	void shouldUseDefaultVersionIfMavenResolverFails() {
		givenBootVersion(SupportedBootVersion.latest().getVersion());
		givenVersionResolverReturns(null);
		GradleBuild gradleBuild = new GradleBuild();
		this.customizer.customize(gradleBuild);
		assertThat(gradleBuild.plugins().values()).anySatisfy(sbomPlugin("1.10.0"));
	}

	@Test
	void shouldUseResolvedVersion() {
		givenBootVersion(SupportedBootVersion.latest().getVersion());
		givenVersionResolverReturns("2.20.0");
		GradleBuild gradleBuild = new GradleBuild();
		this.customizer.customize(gradleBuild);
		assertThat(gradleBuild.plugins().values()).anySatisfy(sbomPlugin("2.20.0"));
	}

	private ThrowingConsumer<GradlePlugin> sbomPlugin(String version) {
		return (plugin) -> {
			assertThat(plugin.getId()).isEqualTo("org.cyclonedx.bom");
			assertThat(plugin).isInstanceOf(StandardGradlePlugin.class);
			assertThat(((StandardGradlePlugin) plugin).getVersion()).isEqualTo(version);
		};
	}

	private void givenVersionResolverReturns(String pluginVersion) {
		Map<String, String> versions = (pluginVersion != null)
				? Map.of("org.cyclonedx:cyclonedx-gradle-plugin", pluginVersion) : Collections.emptyMap();
		given(this.versionResolver.resolveDependencies("org.springframework.boot", "spring-boot-parent",
				getBootVersion()))
			.willReturn(versions);
	}

	private void givenBootVersion(String version) {
		this.description.setPlatformVersion(Version.parse(version));
	}

	private String getBootVersion() {
		return this.description.getPlatformVersion().toString();
	}

}
