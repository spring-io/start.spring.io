/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.metadata.Dependency;
import org.junit.Test;

/**
 * Tests for {@link SpringKafkaRequestPostProcessor}.
 *
 * @author Wonwoo Lee
 * @author Stephane Nicoll
 */
public class SpringKafkaRequestPostProcessorTests
		extends AbstractRequestPostProcessorTests {

	@Test
	public void springKafkaTestIsAdded() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("1.5.0.RELEASE");
		Dependency kafkaTest = Dependency.withId("spring-kafka-test",
				"org.springframework.kafka", "spring-kafka-test", null,
				Dependency.SCOPE_TEST);
		generateMavenPom(request).hasSpringBootStarterTest().hasDependency(kafkaTest)
				.hasDependenciesCount(4);
	}

	@Test
	public void springKafkaTestIsNotAddedWithoutKafka() {
		ProjectRequest request = createProjectRequest("web");
		generateMavenPom(request).hasSpringBootStarterDependency("web")
				.hasSpringBootStarterTest().hasDependenciesCount(2);
	}

	@Test
	public void springKafkaIsOverriddenWith15Maven() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("1.5.0.RELEASE");
		generateMavenPom(request).hasProperty("spring-kafka.version", "1.3.8.RELEASE");
	}

	@Test
	public void springKafkaIsOverriddenWith15Gradle() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("1.5.0.RELEASE");
		generateGradleBuild(request)
				.hasBuildProperties("spring-kafka.version=1.3.8.RELEASE");
	}

	@Test
	public void springKafkaIsNotOverriddenWith20Maven() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("2.0.0.RELEASE");
		generateMavenPom(request).hasNoProperty("spring-kafka.version");
	}

	@Test
	public void springKafkaIsNotOverriddenWith20Gradle() {
		ProjectRequest request = createProjectRequest("kafka");
		request.setBootVersion("2.0.0.RELEASE");
		generateGradleBuild(request).doesNotContain("spring-kafka.version");
	}

}