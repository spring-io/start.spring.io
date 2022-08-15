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
import java.util.function.Predicate;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.documentation.HelpDocument;

/**
 * A dependency that is added when a {@link Build} is in a certain state.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 */
public final class ImplicitDependency {

	private final Predicate<Build> buildPredicate;

	private final Consumer<Build> buildCustomizer;

	private final Consumer<HelpDocument> helpDocumentCustomizer;

	private ImplicitDependency(Builder builder) {
		this.buildPredicate = builder.buildPredicate;
		this.buildCustomizer = builder.buildCustomizer;
		this.helpDocumentCustomizer = builder.helpDocumentCustomizer;
	}

	/**
	 * Customize the specified {@link Build} if necessary.
	 * @param build a build
	 */
	public void customize(Build build) {
		if (this.buildCustomizer != null && this.buildPredicate.test(build)) {
			this.buildCustomizer.accept(build);
		}
	}

	/**
	 * Customize the specified {@link HelpDocument} based on the state of the specified
	 * {@link Build} if necessary.
	 * @param helpDocument a help document
	 * @param build a build
	 */
	public void customize(HelpDocument helpDocument, Build build) {
		if (this.helpDocumentCustomizer != null && this.buildPredicate.test(build)) {
			this.helpDocumentCustomizer.accept(helpDocument);
		}
	}

	/**
	 * Builder for {@link ImplicitDependency}.
	 */
	public static class Builder {

		private Predicate<Build> buildPredicate = (build) -> true;

		private Consumer<Build> buildCustomizer;

		private Consumer<HelpDocument> helpDocumentCustomizer;

		/**
		 * For the implicit dependency created by this builder to be enabled, any of the
		 * specified {@code dependencies} must be present in the build. May be combined
		 * with other matches.
		 * @param dependencies the dependencies to match
		 * @return this for method chaining
		 * @see #match(Predicate)
		 */
		public Builder matchAnyDependencyIds(String... dependencies) {
			return match((build) -> matchAny(build, dependencies));
		}

		/**
		 * For the implicit dependency created by this builder to be enabled, all of the
		 * specified {@code dependencies} must be present in the build. May be combined
		 * with other matches.
		 * @param dependencies the dependencies to match
		 * @return this for method chaining
		 * @see #match(Predicate)
		 */
		public Builder matchAllDependencyIds(String... dependencies) {
			return match((build) -> matchAll(build, dependencies));
		}

		/**
		 * For the implicit dependency created by this builder to be enabled, the
		 * specified {@link Predicate} must pass. May be combined with other matches.
		 * @param buildPredicate the predicate to use to enable the dependency
		 * @return this for method chaining
		 */
		public Builder match(Predicate<Build> buildPredicate) {
			this.buildPredicate = this.buildPredicate.and(buildPredicate);
			return this;
		}

		/**
		 * Set the {@link Build} customizer to use.
		 * @param buildCustomizer the build customizer to use
		 * @return this for method chaining
		 */
		public Builder customizeBuild(Consumer<Build> buildCustomizer) {
			this.buildCustomizer = buildCustomizer;
			return this;
		}

		/**
		 * Set the {@link HelpDocument} customizer to use.
		 * @param helpDocumentCustomizer the help document customizer to use
		 * @return this for method chaining
		 */
		public Builder customizeHelpDocument(Consumer<HelpDocument> helpDocumentCustomizer) {
			this.helpDocumentCustomizer = helpDocumentCustomizer;
			return this;
		}

		/**
		 * Create an {@link ImplicitDependency} based on the state of this builder.
		 * @return an implicit dependency
		 */
		public ImplicitDependency build() {
			return new ImplicitDependency(this);
		}

		private static boolean matchAny(Build build, String... dependencies) {
			for (String dependency : dependencies) {
				if (build.dependencies().has(dependency)) {
					return true;
				}
			}
			return false;
		}

		private static boolean matchAll(Build build, String... dependencies) {
			for (String dependency : dependencies) {
				if (!build.dependencies().has(dependency)) {
					return false;
				}
			}
			return true;
		}

	}

}
