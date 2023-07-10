/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.springmodulith;

import java.util.Arrays;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.support.MetadataBuildItemResolver;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringModulithBuildCustomizer}.
 *
 * @author Oliver Drotbohm
 */
class SpringModulithBuildCustomizerTests extends AbstractExtensionTests {

	private final SpringModulithBuildCustomizer customizer = new SpringModulithBuildCustomizer();

	@Test
	void registersTestStarterWhenModulithIsSelected() {
		Build build = createBuild("modulith");
		this.customizer.customize(build);
		assertThat(build.dependencies().ids()).contains("modulith-starter-test");
	}

	@Test
	void registersActuatorStarterIfActuatorsIsPresent() {
		Build build = createBuild("modulith", "actuator");
		this.customizer.customize(build);
		assertThat(build.dependencies().ids()).contains("modulith-actuator");
	}

	@ParameterizedTest
	@ValueSource(
			strings = { "actuator", "datadog", "graphite", "influx", "new-relic", "prometheus", "wavefront", "zipkin" })
	void registersObservabilityStarterIfObservabilityDependencyIsPresent(String dependency) {
		Build build = createBuild("modulith");
		build.dependencies().add(dependency);
		this.customizer.customize(build);
		assertThat(build.dependencies().ids()).contains("modulith-observability");
	}

	@ParameterizedTest
	@ValueSource(strings = { "jdbc", "jpa", "mongodb" })
	void presenceOfSpringDataModuleAddsModuleEventStarter(String store) {
		Build build = createBuild("modulith");
		build.dependencies().add("data-" + store);
		this.customizer.customize(build);
		assertThat(build.dependencies().ids()).contains("modulith-starter-" + store);
	}

	private Build createBuild(String... dependencies) {
		InitializrMetadata metadata = getMetadata();
		MavenBuild build = new MavenBuild(new MetadataBuildItemResolver(metadata, getDefaultPlatformVersion(metadata)));
		Arrays.stream(dependencies).forEach(build.dependencies()::add);
		return build;
	}

	private Version getDefaultPlatformVersion(InitializrMetadata metadata) {
		return Version.parse(metadata.getBootVersions().getDefault().getId());
	}

}
