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

package io.spring.start.site.extension.dependency.springkafka;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
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
	void shouldAddDependencyToKafkaIfBoot40IsUsed() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "kafka-streams");
		assertThat(mavenPom(request)).hasDependency(getDependency(SupportedBootVersion.V4_0, "kafka"));
	}

	@Test
	void shouldNotAddDependencyToKafkaIfBootBefore40IsUsed() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "kafka-streams");
		Dependency kafka = getDependency(SupportedBootVersion.V3_5, "kafka");
		assertThat(mavenPom(request)).doesNotHaveDependency(kafka.getGroupId(), kafka.getArtifactId());
	}

}
