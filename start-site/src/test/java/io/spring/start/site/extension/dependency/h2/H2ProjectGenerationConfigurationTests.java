/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.h2;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link H2ProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class H2ProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void shouldAddH2ConsoleModule() {
		ProjectRequest request = createProjectRequest("h2");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-h2console", null,
				Dependency.SCOPE_COMPILE);
	}

}
