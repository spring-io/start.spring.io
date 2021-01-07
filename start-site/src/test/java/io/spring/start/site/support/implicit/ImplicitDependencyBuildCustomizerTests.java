/*
 * Copyright 2012-2021 the original author or authors.
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
