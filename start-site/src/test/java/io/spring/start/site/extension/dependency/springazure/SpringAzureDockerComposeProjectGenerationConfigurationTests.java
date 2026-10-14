/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAzureDockerComposeProjectGenerationConfiguration}.
 *
 * @author Eddú Meléndez
 */
class SpringAzureDockerComposeProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	@Test
	void doesNothingWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web", "azure-storage");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsAzuriteService() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "azure-storage");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/azurite.yaml"));
	}

	@Test
	void springAzureDockerComposeDependencyIsAdded() {
		ProjectRequest projectRequest = createProjectRequest(BOOT_VERSION, "docker-compose", "azure-storage");
		assertThat(mavenPom(projectRequest)).hasDependency("com.azure.spring", "spring-cloud-azure-docker-compose",
				null, "runtime");
	}

	@Test
	void shouldNotAddDockerComposeTestcontainersDependencyIfNoSpringAzureDependencyIsSelected() {
		ProjectRequest projectRequest = createProjectRequest("docker-compose", "web");
		assertThat(mavenPom(projectRequest)).doesNotHaveDependency("com.azure.spring",
				"spring-cloud-azure-docker-compose");
	}

}
