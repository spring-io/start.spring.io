/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springsecurity;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringSecurityWebAuthnBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class SpringSecurityWebAuthnBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldDoNothingIfNotSelected() {
		ProjectRequest request = createProjectRequest("web");
		Dependency security = getDependency("security");
		assertThat(mavenPom(request)).doesNotHaveDependency(security.getGroupId(), security.getArtifactId());
	}

	@Test
	void shouldAddSpringSecurityOnBoot40() {
		ProjectRequest request = createProjectRequest("spring-security-webauthn");
		assertThat(mavenPom(request)).hasDependency(getDependency("security"));
	}

}
