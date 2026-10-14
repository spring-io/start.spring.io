/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springamqp;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringRabbitStreamsBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class SpringRabbitStreamsBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldDoNothingIfRabbitMqStreamsIsNotSelected() {
		ProjectRequest project = createProjectRequest("web");
		Dependency rabbitMq = getDependency("rabbitmq");
		assertThat(mavenPom(project)).doesNotHaveDependency(rabbitMq.getGroupId(), rabbitMq.getArtifactId());
	}

	@Test
	void shouldAddRabbitMqIfRabbitMqStreamsIsSelected() {
		ProjectRequest project = createProjectRequest("rabbitmq-streams");
		assertThat(mavenPom(project)).hasDependency(getDependency("rabbitmq"));
	}

}
