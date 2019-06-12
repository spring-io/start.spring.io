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
		generateMavenPom(request).hasSpringBootStarterTest().hasDependency(kafkaTest).hasDependenciesCount(4);
	}

	@Test
	void springKafkaTestIsNotAddedWithoutKafka() {
		ProjectRequest request = createProjectRequest("web");
		generateMavenPom(request).hasSpringBootStarterDependency("web").hasSpringBootStarterTest()
				.hasDependenciesCount(2);
	}

	@Test
	void springKafkaIsOverriddenWith15Maven() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("1.5.0.RELEASE");
		generateMavenPom(request).hasProperty("spring-kafka.version", "1.3.8.RELEASE");
	}

	@Test
	void springKafkaIsOverriddenWith15Gradle() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("1.5.0.RELEASE");
		generateGradleBuild(request).hasProperties("spring-kafka.version", "1.3.8.RELEASE");
	}

	@Test
	void springKafkaIsNotOverriddenWith20Maven() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("2.0.0.RELEASE");
		generateMavenPom(request).doesNotHaveProperty("spring-kafka.version");
	}

	@Test
	void springKafkaIsNotOverriddenWith20Gradle() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("2.0.0.RELEASE");
		generateGradleBuild(request).doesNotContain("spring-kafka.version");
	}

}
