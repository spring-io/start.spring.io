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

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringGrpcProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class SpringGrpcProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final String SPRING_GRPC = "spring-grpc";

	@Test
	void shouldDoNothingIfSpringGrpcIsntSelected() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.grpc", "grpc-services")
			.doesNotHaveDependency("org.springframework.grpc", "spring-grpc-test")
			.doesNotContain("protobuf-maven-plugin")
			.doesNotHaveProperty("grpc.version")
			.doesNotHaveProperty("protobuf-java.version");
		assertThat(generateProject(request)).doesNotContainDirectories("src/main/proto");
	}

	@Test
	void shouldAddAdditionalDependenciesForMaven() {
		ProjectRequest request = createProjectRequest(SPRING_GRPC);
		assertThat(mavenPom(request)).hasDependency("io.grpc", "grpc-services", null, Dependency.SCOPE_COMPILE)
			.hasDependency("org.springframework.grpc", "spring-grpc-test", null, Dependency.SCOPE_TEST);
	}

	@Test
	void shouldAddAdditionalDependenciesForGradle() {
		ProjectRequest request = createProjectRequest(SPRING_GRPC);
		request.setType("gradle-project");
		assertThat(gradleBuild(request)).contains("implementation 'io.grpc:grpc-services'")
			.contains("testImplementation 'org.springframework.grpc:spring-grpc-test'");
	}

	@Test
	void shouldAddGrpcPluginAndConfigurationForGradleGroovy() {
		ProjectRequest request = createProjectRequest(SPRING_GRPC);
		request.setType("gradle-project");
		assertThat(gradleBuild(request)).hasPlugin("com.google.protobuf", "0.9.4").containsIgnoringWhitespaces("""
				protobuf {
					protoc {
						artifact = 'com.google.protobuf:protoc'
					}
					plugins {
						grpc {
							artifact = 'io.grpc:protoc-gen-grpc-java'
						}
					}
					generateProtoTasks {
						all()*.plugins {
							grpc {
								option '@generated=omit'
							}
						}
					}
				}
				""");
	}

	@Test
	void shouldAddGrpcPluginAndConfigurationForGradleKotlin() {
		ProjectRequest request = createProjectRequest(SPRING_GRPC);
		request.setType("gradle-project-kotlin");
		assertThat(gradleKotlinDslBuild(request)).hasPlugin("com.google.protobuf", "0.9.4")
			.contains("import com.google.protobuf.gradle.id")
			.containsIgnoringWhitespaces("""
					protobuf {
						protoc {
							artifact = "com.google.protobuf:protoc"
						}
						plugins {
							id("grpc") {
								artifact = "io.grpc:protoc-gen-grpc-java"
							}
						}
						generateProtoTasks {
							all().forEach {
								it.plugins {
									id("grpc") {
										option("@generated=omit")
									}
								}
							}
						}
					}
					""");
	}

	@Test
	void shouldAddProtobufPluginForMaven() {
		ProjectRequest request = createProjectRequest(SPRING_GRPC);
		assertThat(mavenPom(request)).hasProperty("grpc.version", "1.72.0")
			.hasProperty("protobuf-java.version", "4.30.2")
			.containsIgnoringWhitespaces("""
					<plugin>
						<groupId>io.github.ascopes</groupId>
						<artifactId>protobuf-maven-plugin</artifactId>
						<version>3.4.2</version>
						<configuration>
							<protocVersion>${protobuf-java.version}</protocVersion>
							<binaryMavenPlugins>
								<binaryMavenPlugin>
									<groupId>io.grpc</groupId>
									<artifactId>protoc-gen-grpc-java</artifactId>
									<version>${grpc.version}</version>
									<options>@generated=omit</options>
								</binaryMavenPlugin>
							</binaryMavenPlugins>
						</configuration>
						<executions>
							<execution>
								<id>generate</id>
								<goals>
									<goal>generate</goal>
								</goals>
							</execution>
						</executions>
					</plugin>
					""");
	}

	@Test
	void shouldCreateSrcMainProtoDirectory() {
		ProjectRequest request = createProjectRequest(SPRING_GRPC);
		assertThat(generateProject(request)).containsDirectories("src/main/proto");
	}

	@Test
	void shouldNotReplaceStarterIfWebMvcIsntSelected() {
		ProjectRequest request = createProjectRequest(SPRING_GRPC);
		assertThat(mavenPom(request)).hasDependency("org.springframework.grpc", "spring-grpc-spring-boot-starter", null,
				Dependency.SCOPE_COMPILE);
	}

	@Test
	void shouldReplaceStarterIfWebMvcIsSelected() {
		ProjectRequest request = createProjectRequest(SPRING_GRPC, "web");
		assertThat(mavenPom(request))
			.hasDependency("org.springframework.grpc", "spring-grpc-server-web-spring-boot-starter", null,
					Dependency.SCOPE_COMPILE)
			.doesNotHaveDependency("org.springframework.grpc", "spring-grpc-spring-boot-starter");
	}

}
