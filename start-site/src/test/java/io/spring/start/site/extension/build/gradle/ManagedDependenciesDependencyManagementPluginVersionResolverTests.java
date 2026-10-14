/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.build.gradle;

import java.util.function.Function;

import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.start.site.test.TestMavenVersionResolver;
import org.junit.jupiter.api.Test;

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

	private final MavenVersionResolver versionResolver = TestMavenVersionResolver.get();

	@Test
	@SuppressWarnings("unchecked")
	void dependencyManagementPluginVersionCanBeResolved() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setPlatformVersion(Version.parse("2.1.8.RELEASE"));
		Function<ProjectDescription, String> fallback = mock(Function.class);
		String version = new ManagedDependenciesDependencyManagementPluginVersionResolver(this.versionResolver,
				fallback)
			.resolveDependencyManagementPluginVersion(description);
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
		String version = new ManagedDependenciesDependencyManagementPluginVersionResolver(this.versionResolver,
				fallback)
			.resolveDependencyManagementPluginVersion(description);
		assertThat(version).isEqualTo("1.0.0.RELEASE");
		verify(fallback).apply(description);
	}

}
