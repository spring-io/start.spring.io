/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.stubborn;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.BillOfMaterials;

/**
 * Base {@link BuildCustomizer} for projects containing Stubborn Contract Verifier built
 * with Gradle.
 *
 * @author Moritz Halbritter
 */
abstract class StubbornContractGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final BillOfMaterials bom;

	protected StubbornContractGradleBuildCustomizer(BillOfMaterials bom) {
		this.bom = bom;
	}

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("sh.stubborn.contract", (plugin) -> plugin.setVersion(this.bom.getVersion()));
		if (!build.dependencies().has("webflux")) {
			return;
		}
		configureWebTestClientMode(build);
		build.dependencies()
			.add("rest-assured-spring-web-test-client",
					Dependency.withCoordinates("io.rest-assured", "spring-web-test-client")
						.scope(DependencyScope.TEST_COMPILE));
	}

	/**
	 * Configure the {@code contracts} extension to generate tests using WebTestClient.
	 * @param build the build to customize
	 */
	protected abstract void configureWebTestClientMode(GradleBuild build);

}
