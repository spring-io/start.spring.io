/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Abstract {@link BuildCustomizer Customizer} for a {@link GradleBuild} when the
 * generated project depends on Spring REST Docs.
 *
 * @author Moritz Halbritter
 */
abstract class AbstractSpringRestDocsGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("org.asciidoctor.jvm.convert", (plugin) -> plugin.setVersion("4.0.5"));
		build.properties().property("snippetsDir", "file(\"build/generated-snippets\")");
		customizeForDialect(build);
	}

	abstract void customizeForDialect(GradleBuild build);

}
