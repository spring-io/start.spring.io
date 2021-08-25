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
import io.spring.initializr.generator.buildsystem.gradle.GradlePlugin;
import io.spring.initializr.generator.buildsystem.gradle.StandardGradlePlugin;
import io.spring.initializr.generator.version.VersionReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Shared tests for {@link SpringNativeGradleBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
abstract class SpringNativeGradleBuildCustomizerTests {

	protected abstract SpringNativeGradleBuildCustomizer createCustomizer();

	@Test
	void gradleBuildWithNativeRemoveNativeDependency() {
		SpringNativeGradleBuildCustomizer customizer = createCustomizer();
		GradleBuild build = createBuild("1.0.0");
		customizer.customize(build);
		assertThat(build.dependencies().has("native")).isFalse();
	}

	@Test
	void gradleBuildWithNativeConfigureSpringAotPlugin() {
		SpringNativeGradleBuildCustomizer customizer = createCustomizer();
		GradleBuild build = createBuild("1.0.0");
		customizer.customize(build);
		GradlePlugin springAotPlugin = build.plugins().values()
				.filter((plugin) -> plugin.getId().equals("org.springframework.experimental.aot")).findAny()
				.orElse(null);
		assertThat(springAotPlugin).isNotNull();
		assertThat(springAotPlugin).isInstanceOf(StandardGradlePlugin.class)
				.satisfies((plugin) -> assertThat(((StandardGradlePlugin) plugin).getVersion()).isEqualTo("1.0.0"));
	}

	@Test
	abstract void gradleBuildCustomizeSpringBootPlugin();

	protected GradleBuild createBuild(String springNativeVersion) {
		GradleBuild build = new GradleBuild();
		build.dependencies().add("native", Dependency.withCoordinates("com.example", "native")
				.version(VersionReference.ofValue(springNativeVersion)).build());
		return build;
	}

}
