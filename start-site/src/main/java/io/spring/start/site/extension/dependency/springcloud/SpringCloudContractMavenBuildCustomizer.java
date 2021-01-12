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

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.MavenRepository;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
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

	private static final MavenRepository SPRING_MILESTONES = MavenRepository
			.withIdAndUrl("spring-milestones", "https://repo.spring.io/milestone").name("Spring Milestones").build();

	private static final MavenRepository SPRING_SNAPSHOTS = MavenRepository
			.withIdAndUrl("spring-snapshots", "https://repo.spring.io/snapshot").name("Spring Snapshots")
			.snapshotsEnabled(true).build();

	private final ProjectDescription description;

	private final SpringCloudProjectVersionResolver projectsVersionResolver;

	SpringCloudContractMavenBuildCustomizer(ProjectDescription description,
			SpringCloudProjectVersionResolver projectsVersionResolver) {
		this.description = description;
		this.projectsVersionResolver = projectsVersionResolver;
	}

	@Override
	public void customize(MavenBuild mavenBuild) {
		Version platformVersion = this.description.getPlatformVersion();
		String sccPluginVersion = this.projectsVersionResolver.resolveVersion(platformVersion,
				"org.springframework.cloud:spring-cloud-contract-verifier");
		if (sccPluginVersion == null) {
			logger.warn(
					"Spring Cloud Contract Verifier Maven plugin version could not be resolved for Spring Boot version: "
							+ platformVersion.toString());
			return;
		}
		mavenBuild.plugins().add("org.springframework.cloud", "spring-cloud-contract-maven-plugin", (plugin) -> {
			plugin.extensions(true).version(sccPluginVersion);
			plugin.configuration((builder) -> builder.add("testFramework", "JUNIT5"));
			if (mavenBuild.dependencies().has("webflux")) {
				plugin.configuration((builder) -> builder.add("testMode", "WEBTESTCLIENT"));
				mavenBuild.dependencies().add("rest-assured-spring-web-test-client",
						Dependency.withCoordinates("io.rest-assured", "spring-web-test-client")
								.scope(DependencyScope.TEST_COMPILE));
			}
		});
		configurePluginRepositories(mavenBuild, sccPluginVersion);
	}

	private void configurePluginRepositories(MavenBuild mavenBuild, String sccPluginVersion) {
		Version pluginVersion = Version.parse(sccPluginVersion);
		if (pluginVersion.getQualifier() != null) {
			String qualifier = pluginVersion.getQualifier().getId();
			if (!qualifier.equals("RELEASE")) {
				mavenBuild.pluginRepositories().add(SPRING_MILESTONES);
				if (qualifier.contains("SNAPSHOT")) {
					mavenBuild.pluginRepositories().add(SPRING_SNAPSHOTS);
				}
			}
		}
	}

}
