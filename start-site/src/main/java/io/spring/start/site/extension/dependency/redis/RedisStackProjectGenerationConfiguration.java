/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.redis;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.start.site.container.ComposeFileCustomizer;
import io.spring.start.site.container.DockerServiceResolver;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Redis Stack.
 *
 * @author Eddú Meléndez
 */
@Configuration(proxyBeanMethods = false)
class RedisStackProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer redisStackServiceConnectionsCustomizer(Build build,
			DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> {
			if (isRedisEnabled(build)) {
				serviceResolver.doWith("redisStack", (service) -> serviceConnections
					.addServiceConnection(ServiceConnection.ofGenericContainer("redisStack", service, "redis")));
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer redisStackComposeFileCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (composeFile) -> {
			if (isRedisEnabled(build)) {
				serviceResolver.doWith("redisStack", (service) -> composeFile.services()
					.add("redis", service
						.andThen((builder) -> builder.label("org.springframework.boot.service-connection", "redis"))));
			}
		};
	}

	private boolean isRedisEnabled(Build build) {
		return build.dependencies().has("spring-ai-vectordb-redis")
				|| build.dependencies().has("spring-ai-chat-memory-repository-redis");
	}

}
