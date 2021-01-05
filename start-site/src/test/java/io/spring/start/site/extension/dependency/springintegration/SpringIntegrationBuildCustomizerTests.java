/*
 * Copyright 2012-2021 the original author or authors.
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

package io.spring.start.site.extension.dependency.springintegration;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringIntegrationBuildCustomizer}.
 *
 * @author Artem Bilan
 * @author Stephane Nicoll
 */
class SpringIntegrationBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void springIntegrationTestIsAddedAutomatically() {
		ProjectRequest request = createProjectRequest("integration");
		Dependency springIntegrationTest =
				Dependency.withId("spring-integration-test", "org.springframework.integration",
						"spring-integration-test", null, Dependency.SCOPE_TEST);
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
				.hasDependency(springIntegrationTest).hasDependenciesSize(3);
	}

	@Test
	void springIntegrationModulesAddedIfRequested() {
		ProjectRequest request =
				createProjectRequest("integration",
						"amqp",
						"geode",
						"web",
						"jdbc",
						"data-jpa",
						"activemq",
						"kafka",
						"mail",
						"data-mongodb",
						"data-r2dbc",
						"data-redis-reactive",
						"rsocket",
						"websocket",
						"webflux",
						"security",
						"web-services");

		assertThat(mavenPom(request))
				.hasDependency(integrationDependency("amqp"))
				.hasDependency(integrationDependency("gemfire"))
				.hasDependency(integrationDependency("http"))
				.hasDependency(integrationDependency("jdbc"))
				.hasDependency(integrationDependency("jpa"))
				.hasDependency(integrationDependency("jms"))
				.hasDependency(integrationDependency("kafka"))
				.hasDependency(integrationDependency("mail"))
				.hasDependency(integrationDependency("mongodb"))
				.hasDependency(integrationDependency("r2dbc"))
				.hasDependency(integrationDependency("redis"))
				.hasDependency(integrationDependency("rsocket"))
				.hasDependency(integrationDependency("stomp"))
				.hasDependency(integrationDependency("websocket"))
				.hasDependency(integrationDependency("webflux"))
				.hasDependency(integrationDependency("security"))
				.hasDependency(integrationDependency("ws"));
	}

	private static Dependency integrationDependency(String id) {
		String integrationModule = "spring-integration-" + id;
		return Dependency.withId(integrationModule, "org.springframework.integration", integrationModule, null,
				Dependency.SCOPE_COMPILE);
	}

}
