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
import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.groovy.GroovyLanguage;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependencyBuildCustomizer;
import io.spring.start.site.support.implicit.ImplicitDependencyHelpDocumentCustomizer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Testcontainers.
 *
 * @author Maciej Walkowiak
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("testcontainers")
public class TestcontainersProjectGenerationConfiguration {

	private final Iterable<ImplicitDependency> dependencies;

	public TestcontainersProjectGenerationConfiguration(ProjectDescription projectDescription) {
		this.dependencies = TestcontainersModuleRegistry.create(projectDescription.getPlatformVersion());
	}

	@Bean
	public ImplicitDependencyBuildCustomizer testContainersBuildCustomizer() {
		return new ImplicitDependencyBuildCustomizer(this.dependencies);
	}

	@Bean
	public ImplicitDependencyHelpDocumentCustomizer testcontainersHelpCustomizer(Build build) {
		return new ImplicitDependencyHelpDocumentCustomizer(this.dependencies, build);
	}

	@Bean
	ServiceConnections serviceConnections(ObjectProvider<ServiceConnectionsCustomizer> customizers) {
		ServiceConnections serviceConnections = new ServiceConnections();
		customizers.orderedStream().forEach((customizer) -> customizer.customize(serviceConnections));
		return serviceConnections;
	}

	@Configuration(proxyBeanMethods = false)
	static class SpringBootSupportConfiguration {

		@Bean
		BuildCustomizer<Build> springBootTestcontainersBuildCustomizer() {
			return (build) -> build.dependencies()
				.add("spring-boot-testcontainers",
						Dependency.withCoordinates("org.springframework.boot", "spring-boot-testcontainers")
							.scope(DependencyScope.TEST_COMPILE));
		}

		@Bean
		TestContainersHelpDocumentCustomizer springBootTestcontainersHelpDocumentCustomizer(
				ProjectDescription description, ServiceConnections serviceConnections) {
			return new TestContainersHelpDocumentCustomizer(description, serviceConnections);
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class TestApplicationConfiguration {

		private final ProjectDescription description;

		private final IndentingWriterFactory indentingWriterFactory;

		TestApplicationConfiguration(ProjectDescription description, IndentingWriterFactory indentingWriterFactory) {
			this.description = description;
			this.indentingWriterFactory = indentingWriterFactory;
		}

		@Bean
		@ConditionalOnLanguage(GroovyLanguage.ID)
		GroovyTestContainersApplicationCodeProjectContributor groovyTestContainersApplicationCodeProjectContributor(
				ServiceConnections serviceConnections) {
			return new GroovyTestContainersApplicationCodeProjectContributor(this.indentingWriterFactory,
					this.description, serviceConnections);
		}

		@Bean
		@ConditionalOnLanguage(KotlinLanguage.ID)
		@ConditionalOnPlatformVersion("3.1.1-SNAPSHOT") // https://github.com/spring-projects/spring-boot/issues/35756
		KotlinTestContainersApplicationCodeProjectContributor kotlinTestContainersApplicationCodeProjectContributor(
				ServiceConnections serviceConnections) {
			return new KotlinTestContainersApplicationCodeProjectContributor(this.indentingWriterFactory,
					this.description, serviceConnections);
		}

		@Bean
		@ConditionalOnLanguage(JavaLanguage.ID)
		JavaTestContainersApplicationCodeProjectContributor javaTestContainersApplicationCodeProjectContributor(
				ServiceConnections serviceConnections) {
			return new JavaTestContainersApplicationCodeProjectContributor(this.indentingWriterFactory,
					this.description, serviceConnections);
		}

	}

}
