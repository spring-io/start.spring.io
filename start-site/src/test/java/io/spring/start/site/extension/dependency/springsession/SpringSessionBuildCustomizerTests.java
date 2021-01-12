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

package io.spring.start.site.extension.dependency.springsession;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringSessionBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class SpringSessionBuildCustomizerTests extends AbstractExtensionTests {

	private static final Dependency REDIS = Dependency.withId("session-data-redis", "org.springframework.session",
			"spring-session-data-redis");

	private static final Dependency JDBC = Dependency.withId("session-jdbc", "org.springframework.session",
			"spring-session-jdbc");

	@Test
	void noSessionWithRedis() {
		ProjectRequest request = createProjectRequest("data-redis");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("data-redis"))
				.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
				.hasDependenciesSize(2);
	}

	@Test
	void sessionWithNoStore() {
		ProjectRequest request = createProjectRequest("session", "data-jpa");
		assertThat(mavenPom(request)).hasDependency("org.springframework.session", "spring-session-core")
				.hasDependency(Dependency.createSpringBootStarter("data-jpa"))
				.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
				.hasDependenciesSize(3);
	}

	@Test
	void sessionWithRedis() {
		ProjectRequest request = createProjectRequest("session", "data-redis");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("data-redis"))
				.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST)).hasDependency(REDIS)
				.hasDependenciesSize(3);
	}

	@Test
	void sessionWithRedisReactive() {
		ProjectRequest request = createProjectRequest("session", "data-redis-reactive");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("data-redis-reactive"))
				.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST)).hasDependency(REDIS)
				.hasDependenciesSize(4); // TODO: side effect of `reactor-test`
	}

	@Test
	void sessionWithJdbc() {
		ProjectRequest request = createProjectRequest("session", "jdbc");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("jdbc"))
				.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST)).hasDependency(JDBC)
				.hasDependenciesSize(3);
	}

	@Test
	void sessionWithRedisAndJdbc() {
		ProjectRequest request = createProjectRequest("session", "data-redis", "jdbc");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("data-redis"))
				.hasDependency(Dependency.createSpringBootStarter("jdbc"))
				.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST)).hasDependency(REDIS)
				.hasDependency(JDBC).hasDependenciesSize(5);
	}

}
