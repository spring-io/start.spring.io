/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.project;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the application name in generated projects.
 *
 * @author Moritz Halbritter
 */
class ApplicationNameTests extends AbstractExtensionTests {

	@Test
	void shouldUseDemoApplicationWithDefaultSettings() {
		ProjectRequest request = createProjectRequest("web");
		ProjectStructure project = generateProject(request);
		assertThat(project).filePaths().contains("src/main/java/com/example/demo/DemoApplication.java");
	}

}
