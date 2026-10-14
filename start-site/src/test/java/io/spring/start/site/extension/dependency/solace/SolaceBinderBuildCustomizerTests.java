/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.solace;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SolaceBinderBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class SolaceBinderBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	@Test
	void binderNotAddedWhenSolaceNotSelected() {
		ProjectRequest request = createProjectRequest("cloud-stream");
		ProjectStructure project = generateProject(request);
		assertNoBinder(project);
	}

	@Test
	void binderNotAddedWhenCloudStreamNotSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "solace");
		ProjectStructure project = generateProject(request);
		assertNoBinder(project);
		assertThat(project).mavenBuild().hasDependency(getDependency(BOOT_VERSION, "solace"));
	}

	@Test
	void binderAddedWhenSolaceAndCloudStreamSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "solace", "cloud-stream");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild().hasDependency("com.solace.spring.cloud", "spring-cloud-starter-stream-solace");
	}

	@Test
	void bomAddedWhenSolaceAndCloudStreamSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "solace", "cloud-stream");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild()
			.hasBom("com.solace.spring.cloud", "solace-spring-cloud-bom", "${solace-spring-cloud.version}");
	}

	@Test
	void bomPropertyAddedWhenSolaceAndCloudStreamSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "solace", "cloud-stream");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild()
			.hasProperty("solace-spring-cloud.version",
					getBom("solace-spring-cloud", BOOT_VERSION.getVersion()).getVersion());
	}

	@Test
	void solaceStarterRemovedWhenSolaceAndCloudStreamSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "solace", "cloud-stream");
		ProjectStructure project = generateProject(request);
		Dependency solace = getDependency(BOOT_VERSION, "solace");
		assertThat(project).mavenBuild().doesNotHaveDependency(solace.getGroupId(), solace.getArtifactId());
	}

	private void assertNoBinder(ProjectStructure project) {
		assertThat(project).mavenBuild()
			.doesNotHaveDependency("com.solace.spring.cloud", "spring-cloud-starter-stream-solace")
			.doesNotHaveBom("com.solace.spring.cloud", "solace-spring-cloud-bom")
			.doesNotHaveProperty("solace-spring-cloud.version");
	}

}
