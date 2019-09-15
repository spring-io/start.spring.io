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

package io.spring.start.site.extension.dependency.springamqp;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringRabbitTestBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class SpringRabbitTestBuildCustomizerTests {

	@Test
	void customizeAddsSpringRabbitTest() {
		Build build = new MavenBuild();
		new SpringRabbitTestBuildCustomizer().customize(build);
		assertThat(build.dependencies().ids()).containsOnly("spring-rabbit-test");
		Dependency springRabbitTest = build.dependencies().get("spring-rabbit-test");
		assertThat(springRabbitTest.getGroupId()).isEqualTo("org.springframework.amqp");
		assertThat(springRabbitTest.getArtifactId()).isEqualTo("spring-rabbit-test");
		assertThat(springRabbitTest.getScope()).isEqualTo(DependencyScope.TEST_COMPILE);
	}

}
