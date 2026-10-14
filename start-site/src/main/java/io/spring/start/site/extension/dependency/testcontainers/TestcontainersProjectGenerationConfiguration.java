/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.testcontainers;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.condition.ConditionalOnLanguage;
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
import io.spring.start.site.container.Testcontainers;
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

	private final Testcontainers testcontainers;

	public TestcontainersProjectGenerationConfiguration() {
		this.testcontainers = new Testcontainers();
		this.dependencies = TestcontainersModuleRegistry.create(this.testcontainers);
	}

	@Bean
	Testcontainers testcontainers() {
		return this.testcontainers;
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

		@Bean
		ImportTestcontainersConfigurationTestApplicationTypeCustomizer importTestcontainersConfigurationTestApplicationTypeCustomizer() {
			return new ImportTestcontainersConfigurationTestApplicationTypeCustomizer();
		}

	}

}
