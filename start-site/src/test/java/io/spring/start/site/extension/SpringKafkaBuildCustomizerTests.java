/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringKafkaBuildCustomizer}.
 *
 * @author Wonwoo Lee
 * @author Stephane Nicoll
 */
class SpringKafkaBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void springKafkaTestIsAdded() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("1.5.0.RELEASE");
		Dependency kafkaTest = Dependency.withId("spring-kafka-test", "org.springframework.kafka", "spring-kafka-test",
				null, Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
				.hasDependency(kafkaTest).hasDependenciesSize(4);
	}

	@Test
	void springKafkaTestIsNotAddedWithoutKafka() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("web"))
				.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
				.hasDependenciesSize(2);
	}

	@Test
	void springKafkaIsOverriddenWith15Maven() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("1.5.0.RELEASE");
		assertThat(mavenPom(request)).hasProperty("spring-kafka.version", "1.3.10.RELEASE");
	}

	@Test
	void springKafkaIsOverriddenWith15Gradle() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("1.5.0.RELEASE");
		assertThat(gradleBuild(request)).containsOnlyExtProperties("spring-kafka.version", "1.3.10.RELEASE");
	}

	@Test
	void springKafkaIsNotOverriddenWith20Maven() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("2.0.0.RELEASE");
		assertThat(mavenPom(request)).doesNotHaveProperty("spring-kafka.version");
	}

	@Test
	void springKafkaIsNotOverriddenWith20Gradle() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("2.0.0.RELEASE");
		assertThat(gradleBuild(request)).doesNotContain("spring-kafka.version");
	}

}
