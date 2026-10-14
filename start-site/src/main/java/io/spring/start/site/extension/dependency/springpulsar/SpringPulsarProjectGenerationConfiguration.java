/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springpulsar;

import java.util.Map;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.condition.ProjectGenerationCondition;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import io.spring.start.site.container.Testcontainers;
import io.spring.start.site.container.Testcontainers.Container;
import io.spring.start.site.container.Testcontainers.SupportedContainer;
import io.spring.start.site.extension.dependency.springpulsar.SpringPulsarProjectGenerationConfiguration.OnPulsarDependencyCondition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Configuration for generation of projects that depend on Pulsar.
 *
 * @author Chris Bono
 */
@ProjectGenerationConfiguration
@Conditional(OnPulsarDependencyCondition.class)
class SpringPulsarProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer pulsarServiceConnectionsCustomizer(DockerServiceResolver serviceResolver,
			Testcontainers testcontainers) {
		Container container = testcontainers.getContainer(SupportedContainer.PULSAR);
		return (serviceConnections) -> serviceResolver.doWith("pulsar",
				(service) -> serviceConnections.addServiceConnection(ServiceConnections.ServiceConnection
					.ofContainer("pulsar", service, container.className(), container.generic())));
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer pulsarComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("pulsar",
				(service) -> composeFile.services().add("pulsar", service));
	}

	static class OnPulsarDependencyCondition extends ProjectGenerationCondition {

		@Override
		protected boolean matches(ProjectDescription description, ConditionContext context,
				AnnotatedTypeMetadata metadata) {
			Map<String, Dependency> requestedDependencies = description.getRequestedDependencies();
			return requestedDependencies.containsKey("pulsar");
		}

	}

}
