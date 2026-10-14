/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import org.junit.jupiter.api.Test;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link OnRequestedSpringAiDependencyCondition}.
 *
 * @author Moritz Halbritter
 */
class OnRequestedSpringAiDependencyConditionTests {

	@Test
	void shouldMatchIfSpringAiHasBeenRequested() {
		OnRequestedSpringAiDependencyCondition condition = new OnRequestedSpringAiDependencyCondition();
		MutableProjectDescription description = new MutableProjectDescription();
		description.addDependency("spring-ai-anthropic",
				Dependency.withCoordinates("org.springframework.ai", "spring-ai-anthropic-spring-boot-starter")
					.build());
		assertThat(matches(condition, description)).isTrue();
	}

	@Test
	void shouldNotMatchIfSpringAiHasNotBeenRequested() {
		OnRequestedSpringAiDependencyCondition condition = new OnRequestedSpringAiDependencyCondition();
		MutableProjectDescription description = new MutableProjectDescription();
		description.addDependency("devtools",
				Dependency.withCoordinates("org.springframework.boot", "spring-boot-devtools").build());
		assertThat(matches(condition, description)).isFalse();
	}

	private static boolean matches(OnRequestedSpringAiDependencyCondition condition, ProjectDescription description) {
		return condition.matches(description, mock(ConditionContext.class), mock(AnnotatedTypeMetadata.class));
	}

}
