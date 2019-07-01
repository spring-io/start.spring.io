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

package io.spring.start.site.extension.springcloud;

import java.util.Optional;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link BuildCustomizer} for projects containing Spring Cloud Contract Verifier built
 * with Maven.
 *
 * @author Olga Maciaszek-Sharma
 */
class SpringCloudContractMavenBuildCustomizer
		implements BuildCustomizer<MavenBuild>, SpringCloudContractDependencyVerifier {

	private static final Log LOG = LogFactory.getLog(SpringCloudContractMavenBuildCustomizer.class);

	private final ResolvedProjectDescription description;

	private final SpringCloudProjectsVersionResolver projectsVersionResolver;

	SpringCloudContractMavenBuildCustomizer(ResolvedProjectDescription description,
			SpringCloudProjectsVersionResolver projectsVersionResolver) {
		this.description = description;
		this.projectsVersionResolver = projectsVersionResolver;
	}

	@Override
	public void customize(MavenBuild mavenBuild) {
		if (doesNotContainSCCVerifier(this.description.getRequestedDependencies())) {
			return;
		}
		Version bootVersion = this.description.getPlatformVersion();
		Optional<String> sccPluginVersion = this.projectsVersionResolver.resolveVersion(bootVersion,
				SPRING_CLOUD_CONTRACT_ARTIFACT_ID);
		if (!sccPluginVersion.isPresent()) {
			LOG.warn(
					"Spring Cloud Contract Verifier Maven plugin version could not be resolved for Spring Boot version: "
							+ bootVersion.toString());
			return;
		}
		mavenBuild.plugin("org.springframework.cloud", "spring-cloud-contract-maven-plugin", sccPluginVersion.get())
				.extensions();
	}

}
