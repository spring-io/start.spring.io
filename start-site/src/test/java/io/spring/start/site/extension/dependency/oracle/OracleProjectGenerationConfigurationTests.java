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

package io.spring.start.site.extension.dependency.oracle;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OracleProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class OracleProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void doesNotGenerateComposeYamlWithoutDockerCompose() {
		ProjectRequest request = createProjectRequest("web", "oracle");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve("compose.yaml")).doesNotExist();
	}

	@Test
	void createsOracleXeServiceWithBoot31() {
		ProjectRequest request = createProjectRequest("docker-compose", "oracle");
		request.setBootVersion("3.1.0");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/oracle-xe.yaml"));
	}

	@Test
	void createsOracleFreeServiceWithBoot32() {
		ProjectRequest request = createProjectRequest("docker-compose", "oracle");
		request.setBootVersion("3.2.0");
		assertThat(composeFile(request)).hasSameContentAs(new ClassPathResource("compose/oracle-free.yaml"));
	}

	@Test
	void declaresOracleXeContainerBeanWithBoot31() {
		ProjectRequest request = createProjectRequest("testcontainers", "oracle");
		request.setBootVersion("3.1.0");
		request.setLanguage("java");
		assertThat(generateProject(request)).textFile("src/test/java/com/example/demo/TestDemoApplication.java")
			.contains("import org.testcontainers.containers.OracleContainer;")
			.contains("		return new OracleContainer(DockerImageName.parse(\"gvenzl/oracle-xe:latest\"));");
	}

	@Test
	void declaresOracleFreeContainerBeanWithBoot32() {
		ProjectRequest request = createProjectRequest("testcontainers", "oracle");
		request.setBootVersion("3.2.0");
		request.setLanguage("java");
		assertThat(generateProject(request)).textFile("src/test/java/com/example/demo/TestDemoApplication.java")
			.contains("import org.testcontainers.oracle.OracleContainer;")
			.contains("		return new OracleContainer(DockerImageName.parse(\"gvenzl/oracle-free:latest\"));");
	}

}
