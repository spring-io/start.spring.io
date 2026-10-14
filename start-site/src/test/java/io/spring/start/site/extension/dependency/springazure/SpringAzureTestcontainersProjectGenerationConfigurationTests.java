/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringAzureTestcontainersProjectGenerationConfiguration}.
 *
 * @author Eddú Meléndez
 */
class SpringAzureTestcontainersProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	@Test
	void springAzureTestcontainersDependencyIsAdded() {
		ProjectRequest projectRequest = createProjectRequest(BOOT_VERSION, "testcontainers", "azure-storage");
		assertThat(mavenPom(projectRequest)).hasDependency("com.azure.spring", "spring-cloud-azure-testcontainers",
				null, "test");
	}

	@Test
	void shouldNotAddSpringAzureTestcontainersDependencyIfNoSpringAzureDependencyIsSelected() {
		ProjectRequest projectRequest = createProjectRequest(BOOT_VERSION, "testcontainers", "azure-keyvault");
		assertThat(mavenPom(projectRequest)).doesNotHaveDependency("com.azure.spring",
				"spring-cloud-azure-testcontainers");
	}

}
