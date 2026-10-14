/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAiProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class SpringAiProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void doesNothingWithoutSpringAi() {
		ProjectRequest request = createProjectRequest("web");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild()
			.doesNotHaveDependency("org.springframework.ai", "spring-ai-advisors-vector-store");
	}

}
