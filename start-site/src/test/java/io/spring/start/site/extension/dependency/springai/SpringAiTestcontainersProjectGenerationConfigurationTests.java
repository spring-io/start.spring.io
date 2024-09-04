/*
 * Copyright 2012-2024 the original author or authors.
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

package io.spring.start.site.extension.dependency.springai;

import java.util.stream.Stream;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAiTestcontainersProjectGenerationConfiguration}.
 *
 * @author Eddú Meléndez
 */
class SpringAiTestcontainersProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@ParameterizedTest
	@MethodSource("supportedTestcontainersSpringAiEntriesBuild")
	void springAiTestcontainersDependencyIsAdded(String springAiDependency) {
		ProjectRequest projectRequest = createProject("3.3.0", "testcontainers", springAiDependency);
		assertThat(mavenPom(projectRequest)).hasDependency("org.springframework.ai",
				"spring-ai-spring-boot-testcontainers", null, "test");
	}

	private ProjectRequest createProject(String springBootVersion, String... styles) {
		ProjectRequest projectRequest = createProjectRequest(styles);
		projectRequest.setLanguage("java");
		projectRequest.setBootVersion(springBootVersion);
		return projectRequest;
	}

	static Stream<String> supportedTestcontainersSpringAiEntriesBuild() {
		return Stream.of("spring-ai-vectordb-chroma", "spring-ai-vectordb-milvus", "spring-ai-ollama",
				"spring-ai-vectordb-oracle", "spring-ai-vectordb-qdrant", "spring-ai-vectordb-weaviate");
	}

}
