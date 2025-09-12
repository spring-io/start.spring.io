/*
 * Copyright 2012 - present the original author or authors.
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
		description.addDependency("spring-ai-azure-openai",
				Dependency.withCoordinates("org.springframework.ai", "spring-ai-azure-openai-spring-boot-starter")
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
