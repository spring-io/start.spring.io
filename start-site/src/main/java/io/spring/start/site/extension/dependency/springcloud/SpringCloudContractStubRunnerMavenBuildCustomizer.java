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

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionReference;

/**
 * {@link BuildCustomizer} for projects containing Spring Cloud Contract Stub Runner built
 * with Maven.
 *
 * @author Eddú Meléndez
 */
class SpringCloudContractStubRunnerMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private static final Version VERSION_2_2_0_M2 = Version.parse("2.2.0.M2");

	private final ProjectDescription description;

	SpringCloudContractStubRunnerMavenBuildCustomizer(ProjectDescription description) {
		this.description = description;
	}

	@Override
	public void customize(MavenBuild mavenBuild) {
		if (isKotlin(this.description.getLanguage()) && isSpringCloudVersionAtLeastAfter()) {
			mavenBuild.dependencies().add("kotlin-scripting-compiler-embeddable",
					Dependency.withCoordinates("org.jetbrains.kotlin", "kotlin-scripting-compiler-embeddable")
							.version(VersionReference.ofValue("${kotlin.version}")));
		}
	}

	private boolean isKotlin(Language language) {
		return language instanceof KotlinLanguage;
	}

	private boolean isSpringCloudVersionAtLeastAfter() {
		return (VERSION_2_2_0_M2.compareTo(this.description.getPlatformVersion()) <= 0);
	}

}
