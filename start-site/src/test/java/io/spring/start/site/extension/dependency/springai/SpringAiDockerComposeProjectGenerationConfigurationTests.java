/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springai;

import java.util.stream.Stream;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAiDockerComposeProjectGenerationConfiguration}.
 *
 * @author Eddú Meléndez
 * @author Moritz Halbritter
 */
class SpringAiDockerComposeProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@ParameterizedTest
	@MethodSource("supportedDockerComposeSpringAiEntriesBuild")
	void springAiTestcontainersDependencyIsAdded(String springAiDependency) {
		ProjectRequest projectRequest = createProject("docker-compose", springAiDependency);
		assertThat(mavenPom(projectRequest)).hasDependency("org.springframework.ai",
				"spring-ai-spring-boot-docker-compose", null, "runtime");
	}

	@Test
	void shouldNotAddSpringAiTestcontainersDependencyIfNoSpringAiDependencyIsSelected() {
		ProjectRequest projectRequest = createProject("docker-compose", "web");
		assertThat(mavenPom(projectRequest)).doesNotHaveDependency("org.springframework.ai",
				"spring-ai-spring-boot-docker-compose");
	}

	private ProjectRequest createProject(String... styles) {
		ProjectRequest projectRequest = createProjectRequest(BOOT_VERSION, styles);
		projectRequest.setLanguage("java");
		return projectRequest;
	}

	static Stream<String> supportedDockerComposeSpringAiEntriesBuild() {
		return Stream.of("spring-ai-vectordb-chroma", "spring-ai-vectordb-milvus", "spring-ai-ollama",
				"spring-ai-vectordb-qdrant", "spring-ai-vectordb-weaviate");
	}

}
