/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.dockercompose;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.container.docker.compose.ComposeFile;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.spring.container.docker.compose.ComposeHelpDocumentCustomizer;
import io.spring.initializr.generator.spring.container.docker.compose.ComposeProjectContributor;
import io.spring.start.site.container.ComposeFileCustomizer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Docker Compose.
 *
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("docker-compose")
class DockerComposeProjectGenerationConfiguration {

	@Bean
	ComposeFile dockerComposeFile(ObjectProvider<ComposeFileCustomizer> composeFileCustomizers) {
		ComposeFile composeFile = new ComposeFile();
		composeFileCustomizers.orderedStream().forEach((customizer) -> customizer.customize(composeFile));
		return composeFile;
	}

	@Bean
	ComposeProjectContributor dockerComposeProjectContributor(ComposeFile composeFile,
			IndentingWriterFactory indentingWriterFactory) {
		return new ComposeProjectContributor(composeFile, indentingWriterFactory);
	}

	@Bean
	ComposeHelpDocumentCustomizer dockerComposeHelpDocumentCustomizer(ComposeFile composeFile) {
		return new ComposeHelpDocumentCustomizer(composeFile);
	}

}
