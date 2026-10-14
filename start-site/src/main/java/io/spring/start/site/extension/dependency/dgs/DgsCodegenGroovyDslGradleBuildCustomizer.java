/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} for Gradle Groovy DSL projects using DGS Codegen.
 *
 * @author Brian Clozel
 */
class DgsCodegenGroovyDslGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final String pluginVersion;

	private final String packageName;

	DgsCodegenGroovyDslGradleBuildCustomizer(String pluginVersion, String packageName) {
		this.pluginVersion = pluginVersion;
		this.packageName = packageName;
	}

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("com.netflix.dgs.codegen", (plugin) -> plugin.setVersion(this.pluginVersion));
		build.extensions().customize("generateJava", (generateJava) -> {
			generateJava.attribute("schemaPaths", "[\"${projectDir}/src/main/resources/graphql-client\"]");
			generateJava.attribute("packageName", "'" + this.packageName + ".codegen'");
			generateJava.attribute("generateClient", "true");
		});
	}

}
