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

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

import io.spring.initializr.generator.container.docker.compose.PortMapping;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.CodeBlock;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.groovy.GroovyCompilationUnit;
import io.spring.initializr.generator.language.groovy.GroovyMethodDeclaration;
import io.spring.initializr.generator.language.groovy.GroovySourceCode;
import io.spring.initializr.generator.language.groovy.GroovySourceCodeWriter;
import io.spring.initializr.generator.language.groovy.GroovyTypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.start.site.container.DockerService;
import io.spring.start.site.container.ServiceConnections;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.Testcontainers;
import org.codehaus.plexus.util.StringUtils;

import org.springframework.boot.SpringApplication;

/**
 * {@link TestContainersApplicationCodeProjectContributor} implementation for Groovy.
 *
 * @author Stephane Nicoll
 */
class GroovyTestContainersApplicationCodeProjectContributor extends
		TestContainersApplicationCodeProjectContributor<GroovyTypeDeclaration, GroovyCompilationUnit, GroovySourceCode> {

	GroovyTestContainersApplicationCodeProjectContributor(IndentingWriterFactory indentingWriterFactory,
			ProjectDescription description, ServiceConnections serviceConnections) {
		super(description, serviceConnections, GroovySourceCode::new,
				new GroovySourceCodeWriter(indentingWriterFactory));
	}

	@Override
	protected void contributeCode(GroovySourceCode sourceCode) {
		super.contributeCode(sourceCode);
		customizeApplicationTypeDeclaration(sourceCode, (type) -> {
			type.modifiers(Modifier.PUBLIC);
			type.addMethodDeclaration(GroovyMethodDeclaration.method("main")
				.modifiers(Modifier.PUBLIC | Modifier.STATIC)
				.returning("void")
				.parameters(Parameter.of("args", String[].class))
				.body(CodeBlock.ofStatement("$T.from($L::main).with($T).run(args)", SpringApplication.class,
						getDescription().getApplicationName(), TESTCONTAINERS_CONFIGURATION_CLASS_NAME)));
		});
	}

	@Override
	protected void configureServiceConnection(GroovyTypeDeclaration typeDeclaration,
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

	private GroovyMethodDeclaration usingGenericContainer(String methodName, String imageId, String connectionName,
			Set<PortMapping> ports) {
		String portsParameter = ports.stream()
			.map((port) -> String.valueOf(port.getContainerPort()))
			.collect(Collectors.joining(", "));
		GroovyMethodDeclaration method = GroovyMethodDeclaration.method(methodName)
			.returning("GenericContainer")
			.body(CodeBlock.ofStatement("new $T<>($L).withExposedPorts($L)",
					Testcontainers.GENERIC_CONTAINER_CLASS_NAME, generatedDockerImageNameCode(imageId),
					portsParameter));
		annotateContainerMethod(method, connectionName);
		return method;
	}

	private GroovyMethodDeclaration usingSpecificContainer(String methodName, String imageId, String containerClassName,
			boolean containerClassNameGeneric) {
		String statementFormat = containerClassNameGeneric ? "new $T<>($L)" : "new $T($L)";
		GroovyMethodDeclaration method = GroovyMethodDeclaration.method(methodName)
			.returning(containerClassName)
			.body(CodeBlock.ofStatement(statementFormat, containerClassName, generatedDockerImageNameCode(imageId)));
		annotateContainerMethod(method, null);
		return method;
	}

}
