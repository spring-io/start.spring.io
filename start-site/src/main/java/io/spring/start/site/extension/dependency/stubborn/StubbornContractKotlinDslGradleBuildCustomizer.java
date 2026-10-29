/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.stubborn;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.metadata.BillOfMaterials;

/**
 * {@link StubbornContractGradleBuildCustomizer} implementation for the Kotlin DSL.
 *
 * @author Moritz Halbritter
 */
class StubbornContractKotlinDslGradleBuildCustomizer extends StubbornContractGradleBuildCustomizer {

	private static final String TEST_MODE_CLASS = "sh.stubborn.contract.verifier.config.TestMode";

	StubbornContractKotlinDslGradleBuildCustomizer(BillOfMaterials bom) {
		super(bom);
	}

	@Override
	protected void configureWebTestClientMode(GradleBuild build) {
		build.extensions()
			.customize("contracts",
					(contracts) -> contracts.attributeWithType("testMode", "TestMode.WEBTESTCLIENT", TEST_MODE_CLASS));
	}

}
