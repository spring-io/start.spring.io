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

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link BuildCustomizer} for projects containing Spring Cloud Contract Verifier built
 * with Gradle.
 *
 * @author Olga Maciaszek-Sharma
 * @author Eddú Meléndez
 */
class SpringCloudContractGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private static final Log logger = LogFactory.getLog(SpringCloudContractGradleBuildCustomizer.class);

	private static final Version VERSION_2_2_0 = Version.parse("2.2.0.RELEASE");

	private final ProjectDescription description;

	private final SpringCloudProjectVersionResolver projectsVersionResolver;

	SpringCloudContractGradleBuildCustomizer(ProjectDescription description,
			SpringCloudProjectVersionResolver projectsVersionResolver) {
		this.description = description;
		this.projectsVersionResolver = projectsVersionResolver;
	}

	@Override
	public void customize(GradleBuild build) {
		Version platformVersion = this.description.getPlatformVersion();
		String sccPluginVersion = this.projectsVersionResolver.resolveVersion(platformVersion,
				"org.springframework.cloud:spring-cloud-contract-verifier");
		if (sccPluginVersion == null) {
			logger.warn(
					"Spring Cloud Contract Verifier Gradle plugin version could not be resolved for Spring Boot version: "
							+ platformVersion.toString());
			return;
		}
		build.buildscript((buildscript) -> buildscript
				.dependency("org.springframework.cloud:spring-cloud-contract-gradle-plugin:" + sccPluginVersion));
		build.plugins().apply("spring-cloud-contract");
		if (isSpringBootVersionAtLeastAfter()) {
			build.tasks().customize("contracts", (task) -> task.attribute("targetFramework",
					"org.springframework.cloud.contract.verifier.config.TestFramework.JUNIT5"));
		}
	}

	private boolean isSpringBootVersionAtLeastAfter() {
		return (VERSION_2_2_0.compareTo(this.description.getPlatformVersion()) <= 0);
	}

}
