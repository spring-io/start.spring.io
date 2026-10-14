/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.dgs;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} for Gradle Kotlin DSL projects using DGS Codegen.
 *
 * @author Brian Clozel
 */
class DgsCodegenKotlinDslGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final String pluginVersion;

	private final String packageName;

	DgsCodegenKotlinDslGradleBuildCustomizer(String pluginVersion, String packageName) {
		this.pluginVersion = pluginVersion;
		this.packageName = packageName;
	}

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("com.netflix.dgs.codegen", (plugin) -> plugin.setVersion(this.pluginVersion));
		build.extensions().customize("tasks.generateJava", (generateJava) -> {
			generateJava.invoke("schemaPaths.add", "\"${projectDir}/src/main/resources/graphql-client\"");
			generateJava.attribute("packageName", "\"" + this.packageName + ".codegen\"");
			generateJava.attribute("generateClient", "true");
		});
	}

}
