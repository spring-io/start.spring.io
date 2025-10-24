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
 * Tests for {@link SpringKafkaBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class SpringKafkaBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldNotAddTestDependencyIfKafkaIsNotSelected() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "web");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.kafka", "spring-kafka-test");
	}

	@Test
	void shouldAddTestDependency() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V3_5, "kafka");
		assertThat(mavenPom(request)).hasDependency("org.springframework.kafka", "spring-kafka-test", null,
				Dependency.SCOPE_TEST);
	}

	@Test
	void shouldNotAddTestDependencyForBoot4() {
		ProjectRequest request = createProjectRequest(SupportedBootVersion.V4_0, "kafka");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.springframework.kafka", "spring-kafka-test");
	}

}
