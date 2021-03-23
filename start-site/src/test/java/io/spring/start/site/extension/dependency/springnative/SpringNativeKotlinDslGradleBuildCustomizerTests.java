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

package io.spring.start.site.extension.dependency.springnative;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringNativeKotlinDslGradleBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
public class SpringNativeKotlinDslGradleBuildCustomizerTests extends SpringNativeGradleBuildCustomizerTests {

	@Override
	protected SpringNativeGradleBuildCustomizer createCustomizer() {
		return new SpringNativeKotlinDslGradleBuildCustomizer();
	}

	@Test
	@Override
	void gradleBuildCustomizeSpringBootPlugin() {
		SpringNativeGradleBuildCustomizer customizer = createCustomizer();
		GradleBuild build = createBuild("1.0.0");
		customizer.customize(build);
		assertThat(build.tasks().has("BootBuildImage")).isTrue();
		assertThat(build.tasks().importedTypes())
				.contains("org.springframework.boot.gradle.tasks.bundling.BootBuildImage");
	}

	@Test
	void gradleBuildWithJpaDoesNotAddHibernateEnhancePlugin() {
		SpringNativeGradleBuildCustomizer customizer = createCustomizer();
		GradleBuild build = createBuild("1.0.0");
		build.dependencies().add("data-jpa", Dependency.withCoordinates("org.hibernate", "hibernate"));
		customizer.customize(build);
		assertThat(build.plugins().has("org.hibernate.orm")).isFalse();
		assertThat(build.getSettings().getPluginMappings()).isEmpty();
		assertThat(build.tasks().has("EnhanceTask")).isFalse();
	}

}
