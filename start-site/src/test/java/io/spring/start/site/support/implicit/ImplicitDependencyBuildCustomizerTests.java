/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.support.implicit;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.start.site.support.implicit.ImplicitDependency.Builder;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Tests for {@link ImplicitDependencyBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class ImplicitDependencyBuildCustomizerTests {

	@Test
	void customizerWithMatchingBuildIsInvoked() {
		Consumer<Build> buildCustomizer = mockBuildCustomizer();
		List<ImplicitDependency> dependencies = Collections
			.singletonList(new Builder().matchAnyDependencyIds("test").customizeBuild(buildCustomizer).build());
		Build build = new MavenBuild();
		build.dependencies().add("test", mock(Dependency.class));
		new ImplicitDependencyBuildCustomizer(dependencies).customize(build);
		verify(buildCustomizer).accept(build);
	}

	@Test
	void customizerWithNonMatchingBuildIsNotInvoked() {
		Consumer<Build> buildCustomizer = mockBuildCustomizer();
		List<ImplicitDependency> dependencies = Collections
			.singletonList(new Builder().matchAnyDependencyIds("test").customizeBuild(buildCustomizer).build());
		Build build = new MavenBuild();
		build.dependencies().add("another", mock(Dependency.class));
		new ImplicitDependencyBuildCustomizer(dependencies).customize(build);
		verifyNoInteractions(buildCustomizer);
	}

	@SuppressWarnings("unchecked")
	private Consumer<Build> mockBuildCustomizer() {
		return mock(Consumer.class);
	}

}
