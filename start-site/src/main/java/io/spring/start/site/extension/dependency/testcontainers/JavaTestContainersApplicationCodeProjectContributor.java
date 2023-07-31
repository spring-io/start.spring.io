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

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.CodeBlock;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaCompilationUnit;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaSourceCode;
import io.spring.initializr.generator.language.java.JavaSourceCodeWriter;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.start.site.container.DockerService;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import org.codehaus.plexus.util.StringUtils;

import org.springframework.boot.SpringApplication;
import org.springframework.util.ClassUtils;

/**
 * {@link TestContainersApplicationCodeProjectContributor} implementation for Java.
 *
 * @author Stephane Nicoll
 */
class JavaTestContainersApplicationCodeProjectContributor extends
		TestContainersApplicationCodeProjectContributor<JavaTypeDeclaration, JavaCompilationUnit, JavaSourceCode> {

	JavaTestContainersApplicationCodeProjectContributor(IndentingWriterFactory indentingWriterFactory,
			ProjectDescription description, ServiceConnections serviceConnections) {
		super(description, serviceConnections, JavaSourceCode::new, new JavaSourceCodeWriter(indentingWriterFactory));
	}

	@Override
	protected void contributeCode(JavaSourceCode sourceCode) {
		customizeApplicationTypeDeclaration(sourceCode, (type) -> {
			type.modifiers(Modifier.PUBLIC);
			type.addMethodDeclaration(JavaMethodDeclaration.method("main")
				.modifiers(Modifier.PUBLIC | Modifier.STATIC)
				.returning("void")
				.parameters(Parameter.of("args", String[].class))
				.body(CodeBlock.ofStatement("$T.from($L::main).with($L.class).run(args)", SpringApplication.class,
						getDescription().getApplicationName(), getTestApplicationName())));
		});
	}

	@Override
	protected void configureServiceConnection(JavaTypeDeclaration typeDeclaration,
			ServiceConnection serviceConnection) {
		String methodName = StringUtils.uncapitalise(serviceConnection.id()) + "Container";
		DockerService dockerService = serviceConnection.dockerService();
		String imageId = "%s:%s".formatted(dockerService.getImage(), dockerService.getImageTag());
		if (serviceConnection.isGenericContainer()) {
			typeDeclaration.addMethodDeclaration(usingGenericContainer(methodName, imageId,
					serviceConnection.connectionName(), dockerService.getPorts()));
		}
		else {
			typeDeclaration.addMethodDeclaration(usingSpecificContainer(methodName, imageId,
					serviceConnection.containerClassName(), serviceConnection.containerClassNameGeneric()));
		}
	}

	private JavaMethodDeclaration usingGenericContainer(String methodName, String imageId, String connectionName,
			int... ports) {
		String portsParameter = Arrays.stream(ports).mapToObj(String::valueOf).collect(Collectors.joining(", "));
		JavaMethodDeclaration method = JavaMethodDeclaration.method(methodName)
			.returning("GenericContainer<?>")
			.body(CodeBlock.ofStatement("return new $T<>($L).withExposedPorts($L)",
					"org.testcontainers.containers.GenericContainer", generatedDockerImageNameCode(imageId),
					portsParameter));
		annotateContainerMethod(method, connectionName);
		return method;
	}

	private JavaMethodDeclaration usingSpecificContainer(String methodName, String imageId, String containerClassName,
			boolean containerClassNameGeneric) {
		String returnType = (containerClassNameGeneric) ? ClassUtils.getShortName(containerClassName) + "<?>"
				: containerClassName;
		String statementFormat = (containerClassNameGeneric) ? "return new $T<>($L)" : "return new $T($L)";
		JavaMethodDeclaration method = JavaMethodDeclaration.method(methodName)
			.returning(returnType)
			.body(CodeBlock.ofStatement(statementFormat, containerClassName, generatedDockerImageNameCode(imageId)));
		annotateContainerMethod(method, null);
		return method;
	}

}
