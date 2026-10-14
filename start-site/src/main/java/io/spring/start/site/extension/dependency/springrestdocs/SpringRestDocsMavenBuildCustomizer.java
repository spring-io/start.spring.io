/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer Customizer} for a {@link MavenBuild} when the generated project
 * depends on Spring REST Docs.
 *
 * @author Andy Wilkinson
 */
class SpringRestDocsMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		build.plugins().add("org.asciidoctor", "asciidoctor-maven-plugin", (plugin) -> {
			plugin.version("2.2.1");
			plugin.execution("generate-docs", (execution) -> {
				execution.phase("prepare-package");
				execution.goal("process-asciidoc");
				execution.configuration((configuration) -> {
					configuration.add("backend", "html");
					configuration.add("doctype", "book");
				});
			});
			plugin.dependency("org.springframework.restdocs", "spring-restdocs-asciidoctor",
					"${spring-restdocs.version}");
		});
	}

}
