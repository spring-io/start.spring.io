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

package io.spring.start.site.extension.dependency.springmodulith;

import java.util.Arrays;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.support.MetadataBuildItemResolver;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringModulithBuildCustomizer}.
 *
 * @author Oliver Drotbohm
 * @author Eddú Meléndez
 * @author Moritz Halbritter
 */
class SpringModulithBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void registersTestAndCoreStarterWhenModulithIsSelected() {
		Build build = createBuild(BOOT_VERSION, "modulith");
		assertThat(build.dependencies().ids()).contains("modulith");
		assertThat(build.dependencies().ids()).contains("modulith-starter-test");
	}

	@Test
	void registersActuatorStarterIfActuatorsIsPresent() {
		Build build = createBuild(BOOT_VERSION, "modulith", "actuator");
		assertThat(build.dependencies().ids()).contains("modulith-actuator", "modulith-observability-api",
				"modulith-observability-core");
		assertThat(build.dependencies().ids()).doesNotContain("modulith-starter-insight");
	}

	@Test
	void registersActuatorStarterIfActuatorsIsPresentForOlderBootVersion() {
		Build build = createBuild(SupportedBootVersion.V4_0, "modulith", "actuator");
		assertThat(build.dependencies().ids()).contains("modulith-actuator", "modulith-observability");
		assertThat(build.dependencies().ids()).doesNotContain("modulith-observability-api",
				"modulith-observability-core", "modulith-starter-insight");
	}

	@Test
	void registersRuntimeIfFlywayIsPresent() {
		Build build = createBuild(BOOT_VERSION, "modulith", "flyway");
		assertThat(build.dependencies().ids()).contains("modulith-runtime");
	}

	@ParameterizedTest
	@ValueSource(strings = { "actuator", "datadog", "graphite", "influx", "new-relic", "otlp-metrics", "prometheus",
			"zipkin" })
	void registersObservabilityStarterIfObservabilityDependencyIsPresent(String dependency) {
		Build build = createBuild(BOOT_VERSION, "modulith", dependency);
		assertThat(build.dependencies().ids()).contains("modulith-observability-api", "modulith-observability-core");
		assertThat(build.dependencies().ids()).doesNotContain("modulith-starter-insight");
	}

	@ParameterizedTest
	@ValueSource(strings = { "jdbc", "jpa", "mongodb", "neo4j" })
	void presenceOfSpringDataModuleAddsModuleEventStarter(String store) {
		Build build = createBuild(BOOT_VERSION, "modulith", "data-" + store);
		assertThat(build.dependencies().ids()).contains("modulith-starter-" + store);
		assertThat(build.dependencies().ids()).doesNotContain("modulith-starter-core");
	}

	@ParameterizedTest
	@ValueSource(strings = { "amqp", "kafka" })
	void addsExternalizationDependency(String broker) {
		Build build = createBuild(BOOT_VERSION, "modulith", broker);
		assertThat(build.dependencies().ids()).contains("modulith-events-" + broker);
		assertThat(build.dependencies().ids()).contains("modulith-events-api");
	}

	@ParameterizedTest
	@ValueSource(strings = { "activemq", "artemis" })
	void addsJmsExternalizationDependency(String broker) {
		Build build = createBuild(BOOT_VERSION, "modulith", broker);
		assertThat(build.dependencies().ids()).contains("modulith-events-jms");
		assertThat(build.dependencies().ids()).contains("modulith-events-api");
	}

	@Test
	void addsInsightStarterIfBothActuatorAndObservabilityDependenciesDeclared() {
		Build build = createBuild(BOOT_VERSION, "modulith", "actuator", "otlp-metrics");
		assertThat(build.dependencies().ids()).contains("modulith-starter-insight");
		assertThat(build.dependencies().ids()).doesNotContain("modulith-actuator", "modulith-observability-api",
				"modulith-observability-core");
	}

	@Test
	void addsInsightStarterIfBothActuatorAndObservabilityDependenciesDeclaredForOlderBootVersion() {
		Build build = createBuild(SupportedBootVersion.V4_0, "modulith", "actuator", "otlp-metrics");
		assertThat(build.dependencies().ids()).contains("modulith-starter-insight");
		assertThat(build.dependencies().ids()).doesNotContain("modulith-actuator", "modulith-observability",
				"modulith-observability-api", "modulith-observability-core");
	}

	@Test
	void addsLegacyObservabilityDependenciesForOlderBootVersion() {
		Build build = createBuild(SupportedBootVersion.V4_0, "modulith", "otlp-metrics");
		assertThat(build.dependencies().ids()).contains("modulith-observability");
		assertThat(build.dependencies().ids()).doesNotContain("modulith-observability-api",
				"modulith-observability-core", "modulith-starter-insight");
	}

	private Build createBuild(SupportedBootVersion springBootVersion, String... dependencies) {
		InitializrMetadata metadata = getMetadata();
		Version parsedVersion = Version.parse(springBootVersion.getVersion());
		MavenBuild build = new MavenBuild(new MetadataBuildItemResolver(metadata, parsedVersion));
		Arrays.stream(dependencies).forEach(build.dependencies()::add);
		new SpringModulithBuildCustomizer(parsedVersion).customize(build);
		return build;
	}

}
