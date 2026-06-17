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

package io.spring.start.site.extension.dependency.observability;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservabilityDistributedTracingBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class ObservabilityDistributedTracingBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldAddMicrometerTracingOnBoot4OrLater() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "distributed-tracing");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).hasDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-micrometer-tracing-test",
				null, Dependency.SCOPE_TEST);
	}

	@Test
	void shouldNotAddMicrometerTracingOnBootBefore4() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "distributed-tracing");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-test");
	}

	@Test
	void shouldMaintainCompatibilityIfZipkinIsPresentOnBootBefore4() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "zipkin");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot", "spring-boot-starter-zipkin");
		assertThat(mavenPom(request)).hasDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("io.zipkin.reporter2", "zipkin-reporter-brave");
	}

	@Test
	void shouldNotAddMicrometerTracingIfZipkinIsPresentOnBoot4OrLater() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "zipkin");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-zipkin");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-zipkin-test", null,
				Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-test");
	}

	@Test
	void shouldNotAddMicrometerTracingIfZipkinAndDistributedTracingArePresentOnBoot4OrLater() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "zipkin", "distributed-tracing");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-zipkin");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-zipkin-test", null,
				Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-test");
	}

	@Test
	void shouldNotAddMicrometerTracingIfOpentelemetryIsPresentOnBoot4OrLater() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "opentelemetry");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-opentelemetry");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot",
				"spring-boot-starter-opentelemetry-test", null, Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-test");
	}

	@Test
	void shouldNotAddMicrometerTracingIfOpentelemetryAndDistributedTracingArePresentOnBoot4OrLater() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "opentelemetry",
				"distributed-tracing");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-opentelemetry");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-brave");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.micrometer", "micrometer-tracing-bridge-brave");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot",
				"spring-boot-starter-opentelemetry-test", null, Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.boot",
				"spring-boot-micrometer-tracing-test");
	}

}
