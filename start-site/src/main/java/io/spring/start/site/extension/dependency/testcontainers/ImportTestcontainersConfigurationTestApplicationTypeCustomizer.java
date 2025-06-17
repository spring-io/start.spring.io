/*
 * Copyright 2012 - present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
			.add(ClassName.of("org.springframework.context.annotation.Import"), (annotation) -> annotation.set("value",
					TestContainersApplicationCodeProjectContributor.TESTCONTAINERS_CONFIGURATION_CLASS_NAME));
	}

}
