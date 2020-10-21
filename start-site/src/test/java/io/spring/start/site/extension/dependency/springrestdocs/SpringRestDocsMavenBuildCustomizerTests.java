/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenPlugin.Configuration;
import io.spring.initializr.generator.buildsystem.maven.MavenPlugin.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenPlugin.Execution;
import io.spring.initializr.generator.buildsystem.maven.MavenPlugin.Setting;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringRestDocsMavenBuildCustomizer}.
 *
 * @author Andy Wilkinson
 */
class SpringRestDocsMavenBuildCustomizerTests {

	private final SpringRestDocsMavenBuildCustomizer customizer = new SpringRestDocsMavenBuildCustomizer();

	@Test
	void customizesGradleBuild() {
		MavenBuild build = new MavenBuild();
		this.customizer.customize(build);
		assertThat(build.plugins().values()).singleElement().satisfies((plugin) -> {
			assertThat(plugin.getGroupId()).isEqualTo("org.asciidoctor");
			assertThat(plugin.getArtifactId()).isEqualTo("asciidoctor-maven-plugin");
			assertThat(plugin.getVersion()).isEqualTo("1.5.8");
			assertThat(plugin.getExecutions()).hasSize(1);
			Execution execution = plugin.getExecutions().get(0);
			assertThat(execution.getId()).isEqualTo("generate-docs");
			assertThat(execution.getGoals()).containsExactly("process-asciidoc");
			assertThat(execution.getPhase()).isEqualTo("prepare-package");
			assertThat(execution.getConfiguration()).isNotNull();
			Configuration configuration = execution.getConfiguration();
			assertThat(configuration.getSettings()).hasSize(2);
			Setting backend = configuration.getSettings().get(0);
			assertThat(backend.getName()).isEqualTo("backend");
			assertThat(backend.getValue()).isEqualTo("html");
			Setting doctype = configuration.getSettings().get(1);
			assertThat(doctype.getName()).isEqualTo("doctype");
			assertThat(doctype.getValue()).isEqualTo("book");
			assertThat(plugin.getDependencies()).hasSize(1);
			Dependency dependency = plugin.getDependencies().get(0);
			assertThat(dependency.getGroupId()).isEqualTo("org.springframework.restdocs");
			assertThat(dependency.getArtifactId()).isEqualTo("spring-restdocs-asciidoctor");
			assertThat(dependency.getVersion()).isEqualTo("${spring-restdocs.version}");
		});
	}

}
