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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link BuildCustomizer} for projects containing Spring Cloud Contract Verifier built
 * with Maven.
 *
 * @author Olga Maciaszek-Sharma
 * @author Eddú Meléndez
 */
class SpringCloudContractMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private static final Log logger = LogFactory.getLog(SpringCloudContractMavenBuildCustomizer.class);

	private static final Version VERSION_2_2_0_M2 = Version.parse("2.2.0.M2");

	private final ProjectDescription description;

	private final SpringCloudProjectVersionResolver projectsVersionResolver;

	SpringCloudContractMavenBuildCustomizer(ProjectDescription description,
			SpringCloudProjectVersionResolver projectsVersionResolver) {
		this.description = description;
		this.projectsVersionResolver = projectsVersionResolver;
	}

	@Override
	public void customize(MavenBuild mavenBuild) {
		Version bootVersion = this.description.getPlatformVersion();
		String sccPluginVersion = this.projectsVersionResolver.resolveVersion(bootVersion,
				"org.springframework.cloud:spring-cloud-contract-verifier");
		if (sccPluginVersion == null) {
			logger.warn(
					"Spring Cloud Contract Verifier Maven plugin version could not be resolved for Spring Boot version: "
							+ bootVersion.toString());
			return;
		}
		mavenBuild.plugins().add("org.springframework.cloud", "spring-cloud-contract-maven-plugin", (plugin) -> {
			plugin.version(sccPluginVersion);
			plugin.extensions();

			if (isKotlin(this.description.getLanguage()) && isSpringCloudVersionAtLeastAfter()) {
				plugin.dependency("org.springframework.cloud", "spring-cloud-contract-spec-kotlin", sccPluginVersion);
			}
		});

		if (isKotlin(this.description.getLanguage()) && isSpringCloudVersionAtLeastAfter()) {
			mavenBuild.dependencies().add("spring-cloud-contract-spec-kotlin",
					Dependency.withCoordinates("org.springframework.cloud", "spring-cloud-contract-spec-kotlin"));

			mavenBuild.plugins().add("org.apache.maven.plugins", "maven-compiler-plugin", (plugin) -> {
				plugin.execution("default-compile", (execution) -> execution.phase("none"));
				plugin.execution("default-testCompile", (execution) -> execution.phase("none"));
				plugin.execution("java-compile", (execution) -> {
					execution.phase("compile");
					execution.goal("compile");
				});
				plugin.execution("java-test-compile", (execution) -> {
					execution.phase("test-compile");
					execution.goal("testCompile");
				});
			});
		}
	}

	private boolean isKotlin(Language language) {
		return language instanceof KotlinLanguage;
	}

	private boolean isSpringCloudVersionAtLeastAfter() {
		return (VERSION_2_2_0_M2.compareTo(this.description.getPlatformVersion()) <= 0);
	}

}
