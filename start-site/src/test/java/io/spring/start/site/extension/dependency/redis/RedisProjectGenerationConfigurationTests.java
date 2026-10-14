/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.redis;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RedisProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 * @author Eddú Meléndez
 */
class RedisProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest("web", "data-redis", "data-redis-reactive");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsRedisService() {
		ProjectRequest request = createProjectRequest("docker-compose", "data-redis");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/redis.yaml"));
	}

	@Test
	void createsRedisServiceWhenReactive() {
		ProjectRequest request = createProjectRequest("docker-compose", "data-redis-reactive");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/redis.yaml"));
	}

	@Test
	void doesNotFailWhenBothRedisAndReactiveRedisAreSelected() {
		ProjectRequest request = createProjectRequest("docker-compose", "data-redis", "data-redis-reactive");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/redis.yaml"));
	}

	@Test
	void createsRedisServiceWhenSession() {
		ProjectRequest request = createProjectRequest("docker-compose", "session-data-redis");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/redis.yaml"));
	}

}
