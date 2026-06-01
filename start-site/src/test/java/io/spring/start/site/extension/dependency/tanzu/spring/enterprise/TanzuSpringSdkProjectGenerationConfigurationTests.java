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

package io.spring.start.site.extension.dependency.tanzu.spring.enterprise;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TanzuEnterpriseProjectGenerationConfiguration} with Tanzu Spring SDK.
 *
 * @author Your Name
 */
class TanzuSpringSdkProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void tanzuSpringSdkDependencyIsAddedToMavenProject() {
		ProjectRequest request = createProjectRequest("tanzu-spring-sdk");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild().hasDependency("com.vmware.tanzu.spring", "tanzu-spring-starter");
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
					<dependency>
						<groupId>com.vmware.tanzu.spring</groupId>
						<artifactId>tanzu-spring-sdk-dependencies</artifactId>
						<version>${tanzu-spring-sdk.version}</version>
						<type>pom</type>
						<scope>import</scope>
					</dependency>
				""");
	}

	@Test
	void tanzuSpringSdkDependencyIsAddedToGradleProject() {
		ProjectRequest request = createProjectRequest("tanzu-spring-sdk");
		request.setType("gradle-project");
		assertThat(gradleBuild(request)).contains("implementation 'com.vmware.tanzu.spring:tanzu-spring-starter'")
			.contains("mavenBom \"com.vmware.tanzu.spring:tanzu-spring-sdk-dependencies:${tanzuSpringSdkVersion}\"");
	}

	@Test
	void tanzuSpringSdkAddsEnterpriseHelpDocumentation() {
		ProjectRequest request = createProjectRequest("tanzu-spring-sdk");
		ProjectStructure project = generateProject(request);
		assertThat(project).textFile("HELP.md")
			.containsSubsequence("## VMware Tanzu Spring Enterprise Extensions", "You have selected to add",
					"Tanzu Spring", "enterprise extensions");
	}

}
