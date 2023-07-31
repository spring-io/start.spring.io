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

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.CodeBlock;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.kotlin.KotlinCompilationUnit;
import io.spring.initializr.generator.language.kotlin.KotlinFunctionDeclaration;
import io.spring.initializr.generator.language.kotlin.KotlinModifier;
import io.spring.initializr.generator.language.kotlin.KotlinSourceCode;
import io.spring.initializr.generator.language.kotlin.KotlinSourceCodeWriter;
import io.spring.initializr.generator.language.kotlin.KotlinTypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.start.site.container.DockerService;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import org.codehaus.plexus.util.StringUtils;

import org.springframework.util.ClassUtils;

/**
 * {@link TestContainersApplicationCodeProjectContributor} implementation for Kotlin.
 *
 * @author Stephane Nicoll
 */
class KotlinTestContainersApplicationCodeProjectContributor extends
		TestContainersApplicationCodeProjectContributor<KotlinTypeDeclaration, KotlinCompilationUnit, KotlinSourceCode> {

	KotlinTestContainersApplicationCodeProjectContributor(IndentingWriterFactory indentingWriterFactory,
			ProjectDescription description, ServiceConnections serviceConnections) {
		super(description, serviceConnections, KotlinSourceCode::new,
				new KotlinSourceCodeWriter(indentingWriterFactory));
	}

	@Override
	protected void contributeCode(KotlinSourceCode sourceCode) {
		customizeApplicationTypeDeclaration(sourceCode, (type) -> type.modifiers(KotlinModifier.PUBLIC));
	}

	@Override
	protected void customizeApplicationCompilationUnit(KotlinSourceCode sourceCode,
			Consumer<KotlinCompilationUnit> customizer) {
		super.customizeApplicationCompilationUnit(sourceCode, customizer
			.andThen((compilationUnit) -> compilationUnit.addTopLevelFunction(KotlinFunctionDeclaration.function("main")
				.parameters(Parameter.of("args", "Array<String>"))
				.body(CodeBlock.ofStatement("$T<$L>().$T($L::class).run(*args)",
						"org.springframework.boot.fromApplication", getDescription().getApplicationName(),
						"org.springframework.boot.with", getTestApplicationName())))));
	}

	@Override
	protected void configureServiceConnection(KotlinTypeDeclaration typeDeclaration,
			ServiceConnection serviceConnection) {
		String methodName = StringUtils.uncapitalise(serviceConnection.id()) + "Container";
		DockerService dockerService = serviceConnection.dockerService();
		String imageId = "%s:%s".formatted(dockerService.getImage(), dockerService.getImageTag());
		if (serviceConnection.isGenericContainer()) {
			typeDeclaration.addFunctionDeclaration(usingGenericContainer(methodName, imageId,
					serviceConnection.connectionName(), dockerService.getPorts()));
		}
		else {
			typeDeclaration.addFunctionDeclaration(usingSpecificContainer(methodName, imageId,
					serviceConnection.containerClassName(), serviceConnection.containerClassNameGeneric()));
		}
	}

	private KotlinFunctionDeclaration usingGenericContainer(String functionName, String imageId, String connectionName,
			int... ports) {
		String portsParameter = Arrays.stream(ports).mapToObj(String::valueOf).collect(Collectors.joining(", "));
		KotlinFunctionDeclaration method = KotlinFunctionDeclaration.function(functionName)
			.returning("GenericContainer<*>")
			.body(CodeBlock.ofStatement("return $T($L).withExposedPorts($L)",
					"org.testcontainers.containers.GenericContainer", generatedDockerImageNameCode(imageId),
					portsParameter));
		annotateContainerMethod(method, connectionName);
		return method;
	}

	private KotlinFunctionDeclaration usingSpecificContainer(String functionName, String imageId,
			String containerClassName, boolean containerClassNameGeneric) {
		String returnType = (containerClassNameGeneric) ? ClassUtils.getShortName(containerClassName) + "<*>"
				: containerClassName;
		KotlinFunctionDeclaration method = KotlinFunctionDeclaration.function(functionName)
			.returning(returnType)
			.body(CodeBlock.ofStatement("return $T($L)", containerClassName, generatedDockerImageNameCode(imageId)));
		annotateContainerMethod(method, null);
		return method;
	}

}
