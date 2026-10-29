/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.stubborn;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.BillOfMaterials;

/**
 * {@link BuildCustomizer} for projects containing Stubborn Contract Verifier built with
 * Maven.
 *
 * @author Moritz Halbritter
 */
class StubbornContractMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private final BillOfMaterials bom;

	StubbornContractMavenBuildCustomizer(BillOfMaterials bom) {
		this.bom = bom;
	}

	@Override
	public void customize(MavenBuild build) {
		String version = "${" + this.bom.getVersionProperty().toStandardFormat() + "}";
		boolean webFlux = build.dependencies().has("webflux");
		build.plugins().add("sh.stubborn", "stubborn-contract-maven-plugin", (plugin) -> {
			plugin.extensions(true).version(version);
			if (webFlux) {
				plugin.configuration((configuration) -> configuration.add("testMode", "WEBTESTCLIENT"));
			}
		});
		if (webFlux) {
			build.dependencies()
				.add("rest-assured-spring-web-test-client",
						Dependency.withCoordinates("io.rest-assured", "spring-web-test-client")
							.scope(DependencyScope.TEST_COMPILE));
		}
	}

}
