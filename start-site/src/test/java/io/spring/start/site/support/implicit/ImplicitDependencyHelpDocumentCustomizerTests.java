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
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.start.site.support.implicit.ImplicitDependency.Builder;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Tests for {@link ImplicitDependencyHelpDocumentCustomizer}.
 *
 * @author Stephane Nicoll
 */
class ImplicitDependencyHelpDocumentCustomizerTests {

	private final HelpDocument document = mock(HelpDocument.class);

	@Test
	void customizerWithMatchingBuildIsInvoked() {
		Consumer<HelpDocument> helpDocumentCustomizer = mockHelpDocumentCustomizer();
		List<ImplicitDependency> dependencies = Collections.singletonList(
				new Builder().matchAnyDependencyIds("test").customizeHelpDocument(helpDocumentCustomizer).build());
		Build build = new MavenBuild();
		build.dependencies().add("test", mock(Dependency.class));
		new ImplicitDependencyHelpDocumentCustomizer(dependencies, build).customize(this.document);
		verify(helpDocumentCustomizer).accept(this.document);
	}

	@Test
	void customizerWithNonMatchingBuildIsNotInvoked() {
		Consumer<HelpDocument> helpDocumentCustomizer = mockHelpDocumentCustomizer();
		List<ImplicitDependency> dependencies = Collections.singletonList(
				new Builder().matchAnyDependencyIds("test").customizeHelpDocument(helpDocumentCustomizer).build());
		Build build = new MavenBuild();
		build.dependencies().add("another", mock(Dependency.class));
		new ImplicitDependencyHelpDocumentCustomizer(dependencies, build).customize(this.document);
		verifyNoInteractions(helpDocumentCustomizer);
	}

	@SuppressWarnings("unchecked")
	private Consumer<HelpDocument> mockHelpDocumentCustomizer() {
		return mock(Consumer.class);
	}

}
