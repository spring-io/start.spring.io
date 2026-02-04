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

package io.spring.start.site.extension.dependency.mcpsecurity;

import io.spring.initializr.generator.test.buildsystem.maven.MavenBuildAssert;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.assertj.core.api.AssertProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link McpSecurityBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class McpSecurityBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	@Test
	void shouldDoNothingIfMcpSecurityIsntSelected() {
		AssertProvider<MavenBuildAssert> pom = getPom("web");
		assertThat(pom).doesNotHaveDependency("org.springaicommunity", "mcp-server-security")
			.doesNotHaveDependency("org.springaicommunity", "mcp-client-security")
			.doesNotHaveDependency("org.springaicommunity", "mcp-authorization-server");
	}

	@Test
	void shouldAddDefaultSetupIfNothingElseIsSelected() {
		AssertProvider<MavenBuildAssert> pom = getPom("mcp-security");
		assertThat(pom).hasDependency("org.springframework.ai", "spring-ai-starter-mcp-client");
		assertThat(pom).hasDependency("org.springaicommunity", "mcp-client-security");
		assertThat(pom).hasDependency("org.springframework.boot", "spring-boot-starter-security");
	}

	@Test
	void shouldAddServerSecurityIfMcpServerIsSelected() {
		AssertProvider<MavenBuildAssert> pom = getPom("mcp-security", "spring-ai-mcp-server");
		assertThat(pom).hasDependency("org.springaicommunity", "mcp-server-security");
	}

	@Test
	void shouldAddSpringSecurityIfMcpServerIsSelected() {
		AssertProvider<MavenBuildAssert> pom = getPom("mcp-security", "spring-ai-mcp-server");
		assertThat(pom).hasDependency("org.springframework.boot", "spring-boot-starter-security");
	}

	@Test
	void shouldNotAddSpringSecurityIfResourceServerIsSelected() {
		AssertProvider<MavenBuildAssert> pom = mavenPom(
				createProjectRequest(BOOT_VERSION, "mcp-security", "spring-ai-mcp-server", "oauth2-resource-server"));
		assertThat(pom).doesNotHaveDependency("org.springframework.boot", "spring-boot-starter-security");
	}

	@Test
	void shouldAddClientSecurityIfMcpClientIsSelected() {
		AssertProvider<MavenBuildAssert> pom = getPom("mcp-security", "spring-ai-mcp-client");
		assertThat(pom).hasDependency("org.springaicommunity", "mcp-client-security");
	}

	@Test
	void shouldAddSpringSecurityIfMcpClientIsSelected() {
		AssertProvider<MavenBuildAssert> pom = getPom("mcp-security", "spring-ai-mcp-client");
		assertThat(pom).hasDependency("org.springframework.boot", "spring-boot-starter-security");
	}

	@Test
	void shouldAddAuthServerSecurityIfAuthServerIsSelected() {
		AssertProvider<MavenBuildAssert> pom = getPom("mcp-security", "oauth2-authorization-server");
		assertThat(pom).hasDependency("org.springaicommunity", "mcp-authorization-server");
	}

	private AssertProvider<MavenBuildAssert> getPom(String... dependencies) {
		return mavenPom(createProjectRequest(BOOT_VERSION, dependencies));
	}

}
