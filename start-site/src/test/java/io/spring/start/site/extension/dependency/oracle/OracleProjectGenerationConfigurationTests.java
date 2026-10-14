/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.oracle;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OracleProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 * @author Eddú Meléndez
 */
class OracleProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

	@Test
	void doesNotGenerateComposeYamlWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web", "oracle");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsOracleFreeService() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "oracle");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/oracle-free.yaml"));
	}

	@Test
	void createsOracleFreeServiceSpringAi() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "docker-compose", "spring-ai-vectordb-oracle");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/oracle-free.yaml"));
	}

	@Test
	void declaresOracleFreeContainerBean() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "testcontainers", "oracle");
		request.setLanguage("java");
		assertThat(generateProject(request)).textFile("src/test/java/com/example/demo/TestcontainersConfiguration.java")
			.contains("import org.testcontainers.oracle.OracleContainer;")
			.contains("		return new OracleContainer(DockerImageName.parse(\"gvenzl/oracle-free:latest\"));");
	}

}
