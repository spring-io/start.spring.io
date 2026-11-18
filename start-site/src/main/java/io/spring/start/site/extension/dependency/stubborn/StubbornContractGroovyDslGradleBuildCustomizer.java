/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.dependency.stubborn;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.metadata.BillOfMaterials;

/**
 * {@link StubbornContractGradleBuildCustomizer} implementation for the Groovy DSL.
 *
 * @author Moritz Halbritter
 */
class StubbornContractGroovyDslGradleBuildCustomizer extends StubbornContractGradleBuildCustomizer {

	StubbornContractGroovyDslGradleBuildCustomizer(BillOfMaterials bom) {
		super(bom);
	}

	@Override
	protected void configureWebTestClientMode(GradleBuild build) {
		build.extensions().customize("contracts", (contracts) -> contracts.attribute("testMode", "'WebTestClient'"));
	}

}
