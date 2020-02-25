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

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.test.project.ProjectAssetTester;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SpringAmqpProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 */
class SpringAmqpProjectGenerationConfigurationTests {

	private final ProjectAssetTester projectTester = new ProjectAssetTester()
			.withConfiguration(SpringAmqpProjectGenerationConfiguration.class);

	@Test
	void springAmqpTestWithAmqp() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.addDependency("amqp", mock(Dependency.class));
		this.projectTester.configure(description, (context) -> assertThat(context).getBeans(BuildCustomizer.class)
				.containsKeys("springAmqpTestBuildCustomizer"));
	}

	@Test
	void springAmqpTestWithoutAmqp() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.addDependency("another", mock(Dependency.class));
		this.projectTester.configure(description, (context) -> assertThat(context).getBeans(BuildCustomizer.class)
				.doesNotContainKeys("springAmqpTestBuildCustomizer"));
	}

}
