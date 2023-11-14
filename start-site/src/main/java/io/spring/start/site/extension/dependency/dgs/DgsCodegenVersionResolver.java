/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * Resolve DGS Codegen plugin version to use based on the platform version and build
 * system.
 *
 * @author Brian Clozel
 */
abstract class DgsCodegenVersionResolver {

	static String resolve(InitializrMetadata metadata, Version platformVersion, BuildSystem build) {
		if (GradleBuildSystem.ID.equals(build.id())) {
			return metadata.getDependencies().get("dgs-codegen").resolve(platformVersion).getVersion();
		}
		else if (MavenBuildSystem.ID.equals(build.id())) {
			// https://github.com/deweyjose/graphqlcodegen/releases
			return "1.50";
		}
		throw new IllegalArgumentException("Could not resolve DGS Codegen version for build system " + build.id());
	}

}
