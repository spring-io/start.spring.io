/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.redis;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RedisStackProjectGenerationConfiguration}.
 *
 * @author Eddú Meléndez
 */
class RedisStackProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "spring-ai-vectordb-redis");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsRedisStackService() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "spring-ai-vectordb-redis");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/redis-stack.yaml"));
	}

	@Test
	void createsRedisStackServiceWithChatMemory() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose",
				"spring-ai-chat-memory-repository-redis");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/redis-stack.yaml"));
	}

}
