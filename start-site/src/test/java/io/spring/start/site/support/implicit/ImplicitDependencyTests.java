/*
 * Copyright 2012-2022 the original author or authors.
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

import java.util.function.Consumer;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.start.site.support.implicit.ImplicitDependency.Builder;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Tests for {@link ImplicitDependency}.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 */
class ImplicitDependencyTests {

	@Test
	void customizeBuildWhenPredicateIsTrueInvokesConsumer() {
		Consumer<Build> buildCustomizer = mockBuildCustomizer();
		ImplicitDependency dependency = new Builder().match((build) -> true).customizeBuild(buildCustomizer).build();
		Build build = mock(Build.class);
		dependency.customize(build);
		verify(buildCustomizer).accept(build);
	}

	@Test
	void customizeBuildWhenAllPredicatesAreTrueInvokesConsumer() {
		Consumer<Build> buildCustomizer = mockBuildCustomizer();
		ImplicitDependency dependency = new Builder().match((build) -> true).match((build) -> true)
				.match((build) -> true).customizeBuild(buildCustomizer).build();
		Build build = mock(Build.class);
		dependency.customize(build);
		verify(buildCustomizer).accept(build);
	}

	@Test
	void customizeBuildWhenPredicatesAreNotAllTrueDoesNotInvokeConsumer() {
		Consumer<Build> buildCustomizer = mockBuildCustomizer();
		ImplicitDependency dependency = new Builder().match((build) -> false).match((build) -> true)
				.customizeBuild(buildCustomizer).build();
		Build build = mock(Build.class);
		dependency.customize(build);
		verifyNoInteractions(buildCustomizer);
	}

	@Test
	void customizeBuildWhenPredicateIsFalseDoesNotInvokeConsumer() {
		Consumer<Build> buildCustomizer = mockBuildCustomizer();
		ImplicitDependency dependency = new Builder().match((build) -> false).customizeBuild(buildCustomizer).build();
		Build build = mock(Build.class);
		dependency.customize(build);
		verifyNoInteractions(buildCustomizer);
	}

	@Test
	void customizeBuildWhenPredicateIsTrueAndNoBuildConsumer() {
		ImplicitDependency dependency = new Builder().match((build) -> false).build();
		Build build = mock(Build.class);
		dependency.customize(build);
		verifyNoInteractions(build);
	}

	@Test
	void customizeBuildWithMatchingDependencyInvokesConsumer() {
		Consumer<Build> buildCustomizer = mockBuildCustomizer();
		ImplicitDependency dependency = new Builder().matchAnyDependencyIds("test", "another")
				.customizeBuild(buildCustomizer).build();
		Build build = new MavenBuild();
		build.dependencies().add("another", mock(Dependency.class));
		dependency.customize(build);
		verify(buildCustomizer).accept(build);
	}

	@Test
	void customizeBuildWithNoMatchingDependencyDoesNotInvokeConsumer() {
		Consumer<Build> buildCustomizer = mockBuildCustomizer();
		ImplicitDependency dependency = new Builder().matchAnyDependencyIds("test", "another")
				.customizeBuild(buildCustomizer).build();
		Build build = new MavenBuild();
		build.dependencies().add("no-match", mock(Dependency.class));
		dependency.customize(build);
		verifyNoInteractions(buildCustomizer);
	}

	@Test
	void customizeHelpDocumentWhenPredicateIsTrueInvokesConsumer() {
		Consumer<HelpDocument> buildCustomizer = mockHelpDocumentCustomizer();
		ImplicitDependency dependency = new Builder().match((build) -> true).customizeHelpDocument(buildCustomizer)
				.build();
		HelpDocument helpDocument = mock(HelpDocument.class);
		Build build = mock(Build.class);
		dependency.customize(helpDocument, build);
		verify(buildCustomizer).accept(helpDocument);
	}

	@Test
	void customizeHelpDocumentWhenPredicateIsFalseDoesNotInvokeConsumer() {
		Consumer<HelpDocument> buildCustomizer = mockHelpDocumentCustomizer();
		ImplicitDependency dependency = new Builder().match((build) -> false).customizeHelpDocument(buildCustomizer)
				.build();
		HelpDocument helpDocument = mock(HelpDocument.class);
		Build build = mock(Build.class);
		dependency.customize(helpDocument, build);
		verifyNoInteractions(buildCustomizer);
	}

	@Test
	void customizeHelpDocumentWhenPredicateIsTrueAndNoHelpDocumentConsumer() {
		ImplicitDependency dependency = new Builder().match((build) -> true).build();
		HelpDocument helpDocument = mock(HelpDocument.class);
		Build build = mock(Build.class);
		dependency.customize(helpDocument, build);
		verifyNoInteractions(helpDocument);
	}

	@SuppressWarnings("unchecked")
	private Consumer<Build> mockBuildCustomizer() {
		return mock(Consumer.class);
	}

	@SuppressWarnings("unchecked")
	private Consumer<HelpDocument> mockHelpDocumentCustomizer() {
		return mock(Consumer.class);
	}

}
