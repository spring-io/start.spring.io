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
 * Configuration for generation of projects that depend on Redis.
 *
 * @author Moritz Halbritter
 * @author Eddú Meléndez
 */
@Configuration(proxyBeanMethods = false)
class RedisProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("testcontainers")
	ServiceConnectionsCustomizer redisServiceConnectionsCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (serviceConnections) -> {
			if (isRedisEnabled(build)) {
				serviceResolver.doWith("redis", (service) -> serviceConnections
					.addServiceConnection(ServiceConnection.ofGenericContainer("redis", service, "redis")));
			}
		};
	}

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	ComposeFileCustomizer redisComposeFileCustomizer(Build build, DockerServiceResolver serviceResolver) {
		return (composeFile) -> {
			if (isRedisEnabled(build)) {
				serviceResolver.doWith("redis", (service) -> composeFile.services().add("redis", service));
			}
		};
	}

	private boolean isRedisEnabled(Build build) {
		return (build.dependencies().has("data-redis") || build.dependencies().has("data-redis-reactive")
				|| build.dependencies().has("session-data-redis"));
	}

}
