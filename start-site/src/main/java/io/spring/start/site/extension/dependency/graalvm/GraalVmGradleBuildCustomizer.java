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

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} abstraction for Gradle projects using GraalVM.
 *
 * @author Stephane Nicoll
 */
class GraalVmGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final String nbtVersion;

	protected GraalVmGradleBuildCustomizer(String nbtVersion) {
		this.nbtVersion = nbtVersion;
	}

	@Override
	public final void customize(GradleBuild build) {
		if (this.nbtVersion != null) {
			build.plugins().add("org.graalvm.buildtools.native", (plugin) -> plugin.setVersion(this.nbtVersion));
		}
	}

}
