/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.thymeleaf;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ThymeleafBuildCustomizer}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class ThymeleafBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void thymeleafWithSpringSecurityAndSpringBootAddsExtrasDependency() {
		ProjectRequest projectRequest = createProjectRequest("thymeleaf", "security");
		assertThat(mavenPom(projectRequest)).hasDependency(Dependency.createSpringBootStarter("thymeleaf"))
			.hasDependency(Dependency.withId("thymeleaf-extras-spring-security", "org.thymeleaf.extras",
					"thymeleaf-extras-springsecurity6"));
	}

	@Test
	void thymeleafWithOAuth2ClientAndSpringBootAddsExtrasDependency() {
		ProjectRequest projectRequest = createProjectRequest("thymeleaf", "oauth2-client");
		assertThat(mavenPom(projectRequest)).hasDependency(Dependency.createSpringBootStarter("thymeleaf"))
			.hasDependency(Dependency.withId("thymeleaf-extras-spring-security", "org.thymeleaf.extras",
					"thymeleaf-extras-springsecurity6"));
	}

	@Test
	void thymeleafWithoutSpringSecurityDoesNotAddExtrasDependency() {
		assertThat(mavenPom(createProjectRequest("thymeleaf", "web"))).doesNotHaveDependency("org.thymeleaf.extras",
				"thymeleaf-extras-springsecurity6");
	}

}
