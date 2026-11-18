/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.dependency.springkafka;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringKafkaStreamsBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class SpringKafkaStreamsBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldAddDependencyToKafka() {
		ProjectRequest request = createProjectRequest("kafka-streams");
		assertThat(mavenPom(request)).hasDependency(getDependency("kafka"));
	}

}
