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

import io.spring.initializr.generator.language.ClassName;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.container.DockerService;
import io.spring.start.site.container.ServiceConnections.ServiceConnection;
import io.spring.start.site.container.ServiceConnectionsCustomizer;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the Testcontainers annotation infrastructure. Ensures that the
 * generator can render annotations independently of specific modules.
 *
 * @author Kaique Vieira Soares
 */
@Import(TestcontainersInfrastructureIntegrationTests.InfrastructureTestConfiguration.class)
class TestcontainersInfrastructureIntegrationTests extends AbstractExtensionTests {

	@Test
	void rendersCustomAnnotationsWithAndWithoutParametersInJava() {
		ProjectRequest request = createProjectRequest("testcontainers");
		request.setLanguage("java");
		ProjectStructure project = generateProject(request);
		assertThat(project).textFile("src/test/java/com/example/demo/TestcontainersConfiguration.java")
			.contains("import com.example.SimpleAnno;")
			.contains("import com.example.ParamAnno;")
			.contains("@SimpleAnno")
			.contains("@ParamAnno(stringValue = \"value\", booleanValue = true, intValue = 42)");
	}

	@Test
	void rendersCustomAnnotationsWithAndWithoutParametersInKotlin() {
		ProjectRequest request = createProjectRequest("testcontainers");
		request.setLanguage("kotlin");
		ProjectStructure project = generateProject(request);
		assertThat(project).textFile("src/test/kotlin/com/example/demo/TestcontainersConfiguration.kt")
			.contains("import com.example.SimpleAnno")
			.contains("import com.example.ParamAnno")
			.contains("@SimpleAnno")
			.contains("@ParamAnno(stringValue = \"value\", booleanValue = true, intValue = 42)");
	}

	@Test
	void rendersCustomAnnotationsWithAndWithoutParametersInGroovy() {
		ProjectRequest request = createProjectRequest("testcontainers");
		request.setLanguage("groovy");
		ProjectStructure project = generateProject(request);
		assertThat(project).textFile("src/test/groovy/com/example/demo/TestcontainersConfiguration.groovy")
			.contains("import com.example.SimpleAnno")
			.contains("import com.example.ParamAnno")
			.contains("@SimpleAnno")
			.contains("@ParamAnno(stringValue = \"value\", booleanValue = true, intValue = 42)");
	}

	@TestConfiguration
	static class InfrastructureTestConfiguration {

		@Bean
		ServiceConnectionsCustomizer fakeInfrastructureCustomizer() {
			return (serviceConnections) -> {
				DockerService fakeDockerService = DockerService.withImageAndTag("fake-image:latest")
					.website("https://example.com")
					.build();
				ServiceConnection connection = ServiceConnection
					.ofContainer("fake", fakeDockerService, "com.example.FakeContainer", false)
					.withAnnotation(ClassName.of("com.example.SimpleAnno"))
					.withAnnotation(ClassName.of("com.example.ParamAnno"), (annotation) -> {
						annotation.set("stringValue", "value");
						annotation.set("booleanValue", true);
						annotation.set("intValue", 42);
					});
				serviceConnections.addServiceConnection(connection);
			};
		}

	}

}
