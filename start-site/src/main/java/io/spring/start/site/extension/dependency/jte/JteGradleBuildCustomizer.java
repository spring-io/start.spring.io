/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.dependency.jte;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to configure the jte Gradle plugin.
 *
 * @author Moritz Halbritter
 */
class JteGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	@Override
	public void customize(GradleBuild build) {
		Dependency jteStarter = build.dependencies().get("jte");
		build.plugins().add("gg.jte.gradle", (plugin) -> plugin.setVersion(jteStarter.getVersion().getValue()));
		build.extensions().customize("jte", (jte) -> {
			jte.invoke("generate");
			jte.attribute("binaryStaticContent", "true");
		});
	}

}
