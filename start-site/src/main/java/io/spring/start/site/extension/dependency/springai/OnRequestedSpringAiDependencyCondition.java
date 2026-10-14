/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.condition.ProjectGenerationCondition;
import io.spring.initializr.generator.project.ProjectDescription;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * {@link ProjectGenerationCondition} implementation for
 * {@link ConditionalOnRequestedSpringAiDependency}.
 *
 * @author Moritz Halbritter
 */
class OnRequestedSpringAiDependencyCondition extends ProjectGenerationCondition {

	@Override
	protected boolean matches(ProjectDescription description, ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		for (Dependency dependency : description.getRequestedDependencies().values()) {
			if (dependency.getGroupId().equals("org.springframework.ai")) {
				return true;
			}
		}
		return false;
	}

}
