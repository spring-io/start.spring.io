/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer Customizer} for a Kotlin {@link GradleBuild} when the generated
 * project depends on Spring REST Docs.
 *
 * @author Moritz Halbritter
 */
class SpringRestDocsGradleKotlinBuildCustomizer extends AbstractSpringRestDocsGradleBuildCustomizer {

	@Override
	void customizeForDialect(GradleBuild build) {
		build.tasks().customize("test", (task) -> task.invoke("outputs.dir", "project.extra[\"snippetsDir\"]!!"));
		build.tasks().customize("asciidoctor", (task) -> {
			task.invoke("inputs.dir", "project.extra[\"snippetsDir\"]!!");
			task.invoke("dependsOn", "tasks.test");
		});
	}

}
