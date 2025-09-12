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

package io.spring.start.site.support.implicit;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that customize the build if necessary based on
 * {@link ImplicitDependency implicit dependencies}.
 *
 * @author Stephane Nicoll
 */
public class ImplicitDependencyBuildCustomizer implements BuildCustomizer<Build> {

	private final Iterable<ImplicitDependency> dependencies;

	public ImplicitDependencyBuildCustomizer(Iterable<ImplicitDependency> dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public void customize(Build build) {
		for (ImplicitDependency dependency : this.dependencies) {
			dependency.customize(build);
		}
	}

}
