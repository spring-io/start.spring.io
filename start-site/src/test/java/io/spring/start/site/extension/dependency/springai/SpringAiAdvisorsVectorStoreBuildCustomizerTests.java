/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAiAdvisorsVectorStoreBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class SpringAiAdvisorsVectorStoreBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldAddVectorStoreAdvisorIfModelAndVectorStoreIsSelectedWithBoot4OrLater() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.latest(), "spring-ai-ollama",
				"spring-ai-vectordb-azure");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild().hasDependency("org.springframework.ai", "spring-ai-vector-store-advisor");
	}

	@Test
	void shouldNotAddAdvisorsVectorStoreIfOnlyModelIsSelected() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.latest(), "spring-ai-ollama");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild()
			.doesNotHaveDependency("org.springframework.ai", "spring-ai-advisors-vector-store");
	}

	@Test
	void shouldNotAddAdvisorsVectorStoreIfOnlyVectorStoreIsSelected() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.latest(), "spring-ai-vectordb-azure");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild()
			.doesNotHaveDependency("org.springframework.ai", "spring-ai-advisors-vector-store");
	}

}
