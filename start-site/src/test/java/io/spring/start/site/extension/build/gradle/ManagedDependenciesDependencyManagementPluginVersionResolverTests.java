/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.build.gradle;

import java.nio.file.Path;
import java.util.function.Function;

import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Tests for {@link ManagedDependenciesDependencyManagementPluginVersionResolver}.
 *
 * @author Stephane Nicoll
 */
class ManagedDependenciesDependencyManagementPluginVersionResolverTests {

	private static DependencyManagementVersionResolver dependencyManagementVersionResolver;

	@BeforeAll
	static void setup(@TempDir Path cacheLocation) {
		dependencyManagementVersionResolver = DependencyManagementVersionResolver.withCacheLocation(cacheLocation);
	}

	@Test
	@SuppressWarnings("unchecked")
	void dependencyManagementPluginVersionCanBeResolved() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setPlatformVersion(Version.parse("2.1.8.RELEASE"));
		Function<ProjectDescription, String> fallback = mock(Function.class);
		String version = new ManagedDependenciesDependencyManagementPluginVersionResolver(
				dependencyManagementVersionResolver, fallback).resolveDependencyManagementPluginVersion(description);
		assertThat(version).isEqualTo("1.0.8.RELEASE");
		verifyNoInteractions(fallback);
	}

	@Test
	@SuppressWarnings("unchecked")
	void dependencyManagementPluginVersionUsesFallbackIfNecessary() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setPlatformVersion(Version.parse("2.1.7.RELEASE"));
		Function<ProjectDescription, String> fallback = mock(Function.class);
		given(fallback.apply(description)).willReturn("1.0.0.RELEASE");
		String version = new ManagedDependenciesDependencyManagementPluginVersionResolver(
				dependencyManagementVersionResolver, fallback).resolveDependencyManagementPluginVersion(description);
		assertThat(version).isEqualTo("1.0.0.RELEASE");
		verify(fallback).apply(description);
	}

}
