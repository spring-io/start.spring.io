/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradleTask;
import io.spring.initializr.generator.buildsystem.gradle.Invocation;
import io.spring.initializr.generator.buildsystem.gradle.StandardGradlePlugin;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Tests for {@link SpringRestDocsGradleGroovyBuildCustomizer}.
 *
 * @author Andy Wilkinson
 */
class SpringRestDocsGradleGroovyBuildCustomizerTests {

	private final SpringRestDocsGradleGroovyBuildCustomizer customizer = new SpringRestDocsGradleGroovyBuildCustomizer();

	@Test
	@SuppressWarnings("removal")
	void customizesGradleBuild() {
		GradleBuild build = new GradleBuild();
		this.customizer.customize(build);
		assertThat(build.plugins().values()).singleElement().satisfies((plugin) -> {
			assertThat(plugin.getId()).isEqualTo("org.asciidoctor.jvm.convert");
			assertThat(((StandardGradlePlugin) plugin).getVersion()).isEqualTo("4.0.5");
		});
		assertThat(build.properties().values()).contains(entry("snippetsDir", "file(\"build/generated-snippets\")"));
		GradleTask testTask = build.tasks().get("test");
		assertThat(testTask).isNotNull();
		assertThat(testTask.getInvocations()).hasSize(1);
		Invocation invocation = testTask.getInvocations().get(0);
		assertThat(invocation.getTarget()).isEqualTo("outputs.dir");
		assertThat(invocation.getArguments()).containsExactly("snippetsDir");
		GradleTask asciidoctorTask = build.tasks().get("asciidoctor");
		assertThat(asciidoctorTask).isNotNull();
		assertThat(asciidoctorTask.getInvocations()).hasSize(2);
		Invocation inputsDir = asciidoctorTask.getInvocations().get(0);
		assertThat(inputsDir.getTarget()).isEqualTo("inputs.dir");
		assertThat(inputsDir.getArguments()).containsExactly("snippetsDir");
		Invocation dependsOn = asciidoctorTask.getInvocations().get(1);
		assertThat(dependsOn.getTarget()).isEqualTo("dependsOn");
		assertThat(dependsOn.getArguments()).containsExactly("test");
	}

}
