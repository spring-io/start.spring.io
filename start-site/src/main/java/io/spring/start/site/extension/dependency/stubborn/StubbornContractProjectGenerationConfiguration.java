/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.stubborn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Stubborn Contract Verifier.
 *
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("stubborn-contract-verifier")
class StubbornContractProjectGenerationConfiguration {

	/**
	 * Default contracts location of the Maven plugin.
	 */
	private static final String MAVEN_CONTRACTS_DIRECTORY = "src/test/resources/contracts";

	/**
	 * Default contracts location of the Gradle plugin.
	 */
	private static final String GRADLE_CONTRACTS_DIRECTORY = "src/contractTest/resources/contracts";

	private final InitializrMetadata metadata;

	private final ProjectDescription description;

	StubbornContractProjectGenerationConfiguration(InitializrMetadata metadata, ProjectDescription description) {
		this.metadata = metadata;
		this.description = description;
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	ContractsDirectoryContributor stubbornContractMavenDirectoryContributor() {
		return new ContractsDirectoryContributor(MAVEN_CONTRACTS_DIRECTORY);
	}

	@Bean
	@ConditionalOnBuildSystem(GradleBuildSystem.ID)
	ContractsDirectoryContributor stubbornContractGradleDirectoryContributor() {
		return new ContractsDirectoryContributor(GRADLE_CONTRACTS_DIRECTORY);
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	StubbornContractMavenBuildCustomizer stubbornContractMavenBuildCustomizer() {
		return new StubbornContractMavenBuildCustomizer(resolveBom());
	}

	@Bean
	@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
	StubbornContractGroovyDslGradleBuildCustomizer stubbornContractGroovyDslGradleBuildCustomizer() {
		return new StubbornContractGroovyDslGradleBuildCustomizer(resolveBom());
	}

	@Bean
	@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
	StubbornContractKotlinDslGradleBuildCustomizer stubbornContractKotlinDslGradleBuildCustomizer() {
		return new StubbornContractKotlinDslGradleBuildCustomizer(resolveBom());
	}

	private BillOfMaterials resolveBom() {
		BillOfMaterials bom = this.metadata.getConfiguration().getEnv().getBoms().get("stubborn-contract");
		return bom.resolve(this.description.getPlatformVersion());
	}

	/**
	 * {@link ProjectContributor} that creates the directory holding the contracts.
	 */
	static class ContractsDirectoryContributor implements ProjectContributor {

		private final String directory;

		ContractsDirectoryContributor(String directory) {
			this.directory = directory;
		}

		@Override
		public void contribute(Path projectRoot) throws IOException {
			Files.createDirectories(projectRoot.resolve(this.directory));
		}

	}

}
