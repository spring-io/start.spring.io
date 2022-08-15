/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.start.site.extension.dependency.springazure;

import java.util.stream.Stream;

import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAzureProjectGenerationConfiguration}.
 *
 * @author Andy Wilkinson
 * @author Yonghui Ye
 */
class SpringAzureProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@ParameterizedTest
	@MethodSource("azureDependencies")
	void onlyAzureDependency(String dependencyId) {
		ProjectStructure project = generateProject(dependencyId);
		assertThat(project).mavenBuild()
				.hasBom("com.azure.spring", "spring-cloud-azure-dependencies", "${spring-cloud-azure.version}")
				.hasDependency(getDependency(dependencyId))
				.doesNotHaveDependency("com.azure.spring", "spring-cloud-azure-starter-actuator")
				.doesNotHaveDependency("com.azure.spring", "spring-cloud-azure-trace-sleuth")
				.doesNotHaveDependency("com.azure.spring", "spring-cloud-azure-starter-integration-storage-queue");
		assertThatHelpDocumentOf(project).doesNotContain("https://aka.ms/spring/docs/actuator")
				.doesNotContain("https://aka.ms/spring/docs/sleuth")
				.doesNotContain("https://aka.ms/spring/docs/spring-integration/storage-queue");
	}

	@Test
	void onlyActuator() {
		ProjectStructure project = generateProject("actuator");
		assertThat(project).mavenBuild().doesNotHaveBom("com.azure.spring", "spring-cloud-azure-dependencies")
				.hasDependenciesSize(2).hasDependency("org.springframework.boot", "spring-boot-starter-actuator")
				.hasDependency("org.springframework.boot", "spring-boot-starter-test", null, "test");
	}

	@ParameterizedTest
	@MethodSource("azureDependencies")
	void azureDependencyWithActuator(String dependencyId) {
		ProjectStructure project = generateProject(dependencyId, "actuator");
		assertThat(project).mavenBuild()
				.hasBom("com.azure.spring", "spring-cloud-azure-dependencies", "${spring-cloud-azure.version}")
				.hasDependency(getDependency(dependencyId))
				.hasDependency("com.azure.spring", "spring-cloud-azure-starter-actuator");
		assertThatHelpDocumentOf(project).contains("https://aka.ms/spring/docs/actuator")
				.doesNotContain("https://aka.ms/spring/docs/sleuth")
				.doesNotContain("https://aka.ms/spring/docs/spring-integration/storage-queue");
	}

	@Test
	void onlySleuth() {
		ProjectStructure project = generateProject("cloud-starter-sleuth");
		assertThat(project).mavenBuild().doesNotHaveBom("com.azure.spring", "spring-cloud-azure-dependencies")
				.hasDependenciesSize(2).hasDependency("org.springframework.cloud", "spring-cloud-starter-sleuth")
				.hasDependency("org.springframework.boot", "spring-boot-starter-test", null, "test");
	}

	@ParameterizedTest
	@MethodSource("azureDependencies")
	void azureDependencyWithSleuth(String dependencyId) {
		ProjectStructure project = generateProject(dependencyId, "cloud-starter-sleuth");
		assertThat(project).mavenBuild()
				.hasBom("com.azure.spring", "spring-cloud-azure-dependencies", "${spring-cloud-azure.version}")
				.hasDependency(getDependency(dependencyId))
				.hasDependency("com.azure.spring", "spring-cloud-azure-trace-sleuth");
		assertThatHelpDocumentOf(project).contains("https://aka.ms/spring/docs/sleuth")
				.doesNotContain("https://aka.ms/spring/docs/actuator")
				.doesNotContain("https://aka.ms/spring/docs/spring-integration/storage-queue");
	}

	@Test
	void onlyIntegration() {
		ProjectStructure project = generateProject("integration");
		assertThat(project).mavenBuild().doesNotHaveBom("com.azure.spring", "spring-cloud-azure-dependencies")
				.hasDependenciesSize(3).hasDependency("org.springframework.boot", "spring-boot-starter-integration")
				.hasDependency("org.springframework.boot", "spring-boot-starter-test", null, "test")
				.hasDependency("org.springframework.integration", "spring-integration-test", null, "test");
	}

	@Test
	void azureStorageWithIntegration() {
		ProjectStructure project = generateProject("azure-storage", "integration");
		assertThat(project).mavenBuild()
				.hasBom("com.azure.spring", "spring-cloud-azure-dependencies", "${spring-cloud-azure.version}")
				.hasDependency(getDependency("azure-storage")).hasDependency(getDependency("integration"))
				.hasDependency("com.azure.spring", "spring-cloud-azure-starter-integration-storage-queue");
		assertThatHelpDocumentOf(project).contains("https://aka.ms/spring/docs/spring-integration/storage-queue")
				.doesNotContain("https://aka.ms/spring/docs/actuator")
				.doesNotContain("https://aka.ms/spring/docs/sleuth");
	}

	private static Stream<Arguments> azureDependencies() {
		return Stream.of(Arguments.of("azure-active-directory"), Arguments.of("azure-cosmos-db"),
				Arguments.of("azure-keyvault"), Arguments.of("azure-storage"), Arguments.of("azure-support"));
	}

	private ProjectStructure generateProject(String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setType("maven-build");
		return generateProject(request);
	}

	private TextAssert assertThatHelpDocumentOf(ProjectStructure project) {
		return new TextAssert(project.getProjectDirectory().resolve("HELP.md"));
	}

}
