/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.properties;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultApplicationPropertiesCustomizer}.
 *
 * @author Moritz Halbritter
 */
class DefaultApplicationPropertiesCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldUseNameForSpringApplicationName() {
		ProjectRequest request = createProjectRequest("web");
		request.setName("my-name");
		assertThat(applicationProperties(request)).lines().contains("spring.application.name=my-name");
	}

	@Test
	void shouldFallbackToArtifactIdWhenNameIsEmpty() {
		ProjectRequest request = createProjectRequest("web");
		request.setName("");
		request.setArtifactId("my-artifact");
		assertThat(applicationProperties(request)).lines().contains("spring.application.name=my-artifact");
	}

	@Test
	void defaultSetupShouldHaveSpringApplicationName() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(applicationProperties(request)).lines().contains("spring.application.name=demo");
	}

}
