/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.testcontainers;

import io.spring.initializr.generator.language.ClassName;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.TestApplicationTypeCustomizer;

/**
 * {@link TestApplicationTypeCustomizer} that imports the generated Testcontainers test
 * configuration.
 *
 * @author Moritz Halbritter
 */
class ImportTestcontainersConfigurationTestApplicationTypeCustomizer
		implements TestApplicationTypeCustomizer<TypeDeclaration> {

	@Override
	public void customize(TypeDeclaration typeDeclaration) {
		typeDeclaration.annotations()
			.addSingle(ClassName.of("org.springframework.context.annotation.Import"), (annotation) -> annotation
				.set("value", TestContainersApplicationCodeProjectContributor.TESTCONTAINERS_CONFIGURATION_CLASS_NAME));
	}

}
