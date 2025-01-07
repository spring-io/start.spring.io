/*
 * Copyright 2012-2025 the original author or authors.
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
			.doesNotContain("os-maven-plugin")
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
								option 'jakarta_omit'
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
										option("jakarta_omit")
										option("@generated=omit")
									}
								}
							}
						}
					}
					""");
	}

	@Test
	void shouldAddOsPluginForMaven() {
		ProjectRequest request = createProjectRequest(SPRING_GRPC);
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
				<plugin>
					<groupId>kr.motd.maven</groupId>
					<artifactId>os-maven-plugin</artifactId>
					<version>1.7.1</version>
					<executions>
						<execution>
							<id>initialize</id>
							<phase>initialize</phase>
							<goals>
								<goal>detect</goal>
							</goals>
						</execution>
					</executions>
				</plugin>
				""");
	}

	@Test
	void shouldAddProtobufPluginForMaven() {
		ProjectRequest request = createProjectRequest(SPRING_GRPC);
		assertThat(mavenPom(request)).hasProperty("grpc.version", "1.69.0")
			.hasProperty("protobuf-java.version", "3.25.5")
			.containsIgnoringWhitespaces(
					"""
							<plugin>
								<groupId>org.xolstice.maven.plugins</groupId>
								<artifactId>protobuf-maven-plugin</artifactId>
								<version>0.6.1</version>
								<configuration>
									<protocArtifact>com.google.protobuf:protoc:${protobuf-java.version}:exe:${os.detected.classifier}</protocArtifact>
									<pluginId>grpc-java</pluginId>
									<pluginArtifact>io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}</pluginArtifact>
								</configuration>
								<executions>
									<execution>
										<id>compile</id>
										<goals>
											<goal>compile</goal>
											<goal>compile-custom</goal>
										</goals>
										<configuration>
											<pluginParameter>jakarta_omit,@generated=omit</pluginParameter>
										</configuration>
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

}
