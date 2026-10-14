/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.activemq;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import io.spring.start.site.container.Testcontainers;
import io.spring.start.site.container.Testcontainers.Container;
import io.spring.start.site.container.Testcontainers.SupportedContainer;

import org.springframework.context.annotation.Bean;

/**
 * Configuration for generation of projects that depend on ActiveMQ.
 *
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("activemq")
public class ActiveMQProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer activeMQServiceConnectionsCustomizer(DockerServiceResolver serviceResolver,
			Testcontainers testcontainers) {
		Container container = testcontainers.getContainer(SupportedContainer.ACTIVEMQ);
		return (serviceConnections) -> serviceResolver
			.doWith("apacheActiveMQ", (service) -> serviceConnections.addServiceConnection(
					ServiceConnection.ofContainer("activemq", service, container.className(), container.generic())));
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer activeMQComposeFileCustomizer(DockerServiceResolver serviceResolver) {
		return (composeFile) -> serviceResolver.doWith("apacheActiveMQ",
				(service) -> composeFile.services().add("activemq", service));
	}

}
