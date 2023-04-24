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

package io.spring.start.site.extension.dependency.testcontainers;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependencyBuildCustomizer;
import io.spring.start.site.support.implicit.ImplicitDependencyHelpDocumentCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Testcontainers.
 *
 * @author Maciej Walkowiak
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("testcontainers")
public class TestcontainersProjectGenerationConfiguration {

	private final Iterable<ImplicitDependency> dependencies;

	public TestcontainersProjectGenerationConfiguration() {
		this.dependencies = TestcontainersModuleRegistry.create();
	}

	@Bean
	public ImplicitDependencyBuildCustomizer testContainersBuildCustomizer() {
		return new ImplicitDependencyBuildCustomizer(this.dependencies);
	}

	@Bean
	public ImplicitDependencyHelpDocumentCustomizer testcontainersHelpCustomizer(Build build) {
		return new ImplicitDependencyHelpDocumentCustomizer(this.dependencies, build);
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnPlatformVersion("3.1.0-RC1")
	static class SpringBootSupportConfiguration {

		@Bean
		BuildCustomizer<Build> springBootTestcontainersBuildCustomizer() {
			return (build) -> build.dependencies()
				.add("spring-boot-testcontainers",
						Dependency.withCoordinates("org.springframework.boot", "spring-boot-testcontainers")
							.scope(DependencyScope.TEST_COMPILE));

		}

		@Bean
		HelpDocumentCustomizer springBootTestcontainersHelpDocumentCustomizer(ProjectDescription description) {
			return (helpDocument) -> {
				String referenceDocUrl = String.format(
						"https://docs.spring.io/spring-boot/docs/%s/reference/html/features.html#features.testing.testcontainers",
						description.getPlatformVersion());
				helpDocument.gettingStarted()
					.addReferenceDocLink(referenceDocUrl, "Spring Boot Testcontainers support");
			};
		}

	}

}
