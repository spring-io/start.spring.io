/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.datasourcemicrometer;

import io.spring.initializr.generator.test.buildsystem.maven.MavenBuildAssert;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.AssertProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DatasourceMicrometerProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class DatasourceMicrometerProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	@Test
	void shouldNotAddOtelModuleByDefault() {
		AssertProvider<MavenBuildAssert> pom = mavenPom(createProjectRequest(BOOT_VERSION, "datasource-micrometer"));
		assertThat(pom).doesNotHaveDependency("net.ttddyy.observation", "datasource-micrometer-opentelemetry");
	}

	@Test
	void shouldAddOtelModuleIfOpenTelemetryIsSelected() {
		AssertProvider<MavenBuildAssert> pom = mavenPom(
				createProjectRequest(BOOT_VERSION, "datasource-micrometer", "opentelemetry"));
		assertThat(pom).hasDependency("net.ttddyy.observation", "datasource-micrometer-opentelemetry");
	}

}
