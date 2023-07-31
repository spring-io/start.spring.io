/*
 * Copyright 2012-2023 the original author or authors.
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

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.spring.initializr.generator.language.Annotatable;
import io.spring.initializr.generator.language.ClassName;
import io.spring.initializr.generator.language.CodeBlock;
import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.SourceCodeWriter;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;

/**
 * Project contributor for the test application used by testcontainers.
 *
 * @param <T> language-specific type declaration
 * @param <C> language-specific compilation unit
 * @param <S> language-specific source code
 * @author Stephane Nicoll
 */
abstract class TestContainersApplicationCodeProjectContributor<T extends TypeDeclaration, C extends CompilationUnit<T>, S extends SourceCode<T, C>>
		implements ProjectContributor {

	public static final ClassName TEST_CONFIGURATION_CLASS_NAME = ClassName
		.of("org.springframework.boot.test.context.TestConfiguration");

	public static final ClassName BEAN_CLASS_NAME = ClassName.of("org.springframework.context.annotation.Bean");

	public static final ClassName SERVICE_CONNECTION_CLASS_NAME = ClassName
		.of("org.springframework.boot.testcontainers.service.connection.ServiceConnection");

	private static final ClassName DOCKER_IMAGE_NAME_CLASS_NAME = ClassName
		.of("org.testcontainers.utility.DockerImageName");

	private final ProjectDescription description;

	private final ServiceConnections serviceConnections;

	private final Supplier<S> sourceFactory;

	private final SourceCodeWriter<S> sourceWriter;

	TestContainersApplicationCodeProjectContributor(ProjectDescription description,
			ServiceConnections serviceConnections, Supplier<S> sourceFactory, SourceCodeWriter<S> sourceWriter) {
		this.description = description;
		this.serviceConnections = serviceConnections;
		this.sourceFactory = sourceFactory;
		this.sourceWriter = sourceWriter;
	}

	protected ProjectDescription getDescription() {
		return this.description;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		S sourceCode = this.sourceFactory.get();
		contributeCode(sourceCode);
		this.sourceWriter.writeTo(
				this.description.getBuildSystem().getTestSource(projectRoot, this.description.getLanguage()),
				sourceCode);
	}

	/**
	 * Contribute code using the specified {@link SourceCode}.
	 * @param sourceCode the source code to use for contributions
	 */
	protected abstract void contributeCode(S sourceCode);

	protected abstract void configureServiceConnection(T typeDeclaration, ServiceConnection serviceConnection);

	protected void customizeApplicationCompilationUnit(S sourceCode, Consumer<C> customizer) {
		C compilationUnit = sourceCode.createCompilationUnit(this.description.getPackageName(),
				getTestApplicationName());
		customizer.accept(compilationUnit);
	}

	protected void customizeApplicationTypeDeclaration(S sourceCode, Consumer<T> customizer) {
		customizeApplicationCompilationUnit(sourceCode, (compilationUnit) -> {
			T applicationType = compilationUnit.createTypeDeclaration(getTestApplicationName());
			applicationType.annotations()
				.add(TEST_CONFIGURATION_CLASS_NAME, (annotation) -> annotation.set("proxyBeanMethods", false));
			this.serviceConnections.values()
				.forEach((serviceConnection) -> configureServiceConnection(applicationType, serviceConnection));
			customizer.accept(applicationType);
		});
	}

	protected void annotateContainerMethod(Annotatable annotable, String name) {
		annotable.annotations().add(BEAN_CLASS_NAME);
		annotable.annotations().add(SERVICE_CONNECTION_CLASS_NAME, (annotation) -> {
			if (name != null) {
				annotation.set("name", name);
			}
		});
	}

	protected String getTestApplicationName() {
		return "Test" + this.description.getApplicationName();
	}

	protected CodeBlock generatedDockerImageNameCode(String imageId) {
		return CodeBlock.of("$T.parse($S)", DOCKER_IMAGE_NAME_CLASS_NAME, imageId);
	}

}
