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
 * Tests for {@link SpringAiTestcontainersProjectGenerationConfiguration}.
 *
 * @author Eddú Meléndez
 * @author Moritz Halbritter
 */
class SpringAiTestcontainersProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@ParameterizedTest
	@MethodSource("supportedTestcontainersSpringAiEntriesBuild")
	void springAiTestcontainersDependencyIsAdded(String springAiDependency) {
		ProjectRequest projectRequest = createProject("testcontainers", springAiDependency);
		assertThat(mavenPom(projectRequest)).hasDependency("org.springframework.ai",
				"spring-ai-spring-boot-testcontainers", null, "test");
	}

	@Test
	void shouldNotAddSpringAiTestcontainersDependencyIfNoSpringAiDependencyIsSelected() {
		ProjectRequest projectRequest = createProject("testcontainers", "web");
		assertThat(mavenPom(projectRequest)).doesNotHaveDependency("org.springframework.ai",
				"spring-ai-spring-boot-testcontainers");
	}

	static Stream<String> supportedTestcontainersSpringAiEntriesBuild() {
		return Stream.of("spring-ai-vectordb-chroma", "spring-ai-vectordb-milvus", "spring-ai-ollama",
				"spring-ai-vectordb-qdrant", "spring-ai-vectordb-weaviate");
	}

	private ProjectRequest createProject(String... styles) {
		ProjectRequest projectRequest = createProjectRequest(BOOT_VERSION, styles);
		projectRequest.setLanguage("java");
		return projectRequest;
	}

}
