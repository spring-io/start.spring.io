/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.jte;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to configure the jte Maven plugin.
 *
 * @author Moritz Halbritter
 */
class JteMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		Dependency jteStarter = build.dependencies().get("jte");
		build.plugins().add("gg.jte", "jte-maven-plugin", (plugin) -> {
			plugin.version(jteStarter.getVersion().getValue());
			plugin.execution("jte-generate", (execution) -> {
				execution.phase("generate-sources");
				execution.goal("generate");
				execution.configuration((configuration) -> {
					configuration.add("sourceDirectory", "${project.basedir}/src/main/jte");
					configuration.add("contentType", "Html");
					configuration.add("binaryStaticContent", "true");
					configuration.add("targetResourceDirectory", "${project.build.outputDirectory}");
				});
			});
		});
	}

}
