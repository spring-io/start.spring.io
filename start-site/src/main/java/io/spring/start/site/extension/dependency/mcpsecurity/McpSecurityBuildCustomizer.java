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

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} which adds {@code mcp-server-security},
 * {@code mcp-client-security} and {@code mcp-authorization-server} depending on which
 * other dependencies are selected.
 * <ul>
 * <li>{@code mcp-server-security} is added if {@code spring-ai-mcp-server} is
 * selected</li>
 * <li>{@code mcp-client-security} is added if {@code spring-ai-mcp-client} is
 * selected</li>
 * <li>{@code mcp-authorization-server} is added if {@code oauth2-authorization-server} is
 * selected</li>
 * </ul>
 *
 * @author Moritz Halbritter
 */
class McpSecurityBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		Dependency dependency = build.dependencies().get("mcp-security");
		if (dependency == null) {
			return;
		}
		build.dependencies().remove("mcp-security");
		boolean hasMcpServer = false;
		boolean hasMcpClient = false;
		boolean hasAuthServer = false;
		if (build.dependencies().has("spring-ai-mcp-server")) {
			hasMcpServer = true;
			handleMcpServer(build, dependency);
		}
		if (build.dependencies().has("spring-ai-mcp-client")) {
			hasMcpClient = true;
			handleMcpClient(build, dependency);
		}
		if (build.dependencies().has("oauth2-authorization-server")) {
			hasAuthServer = true;
			handleOAuth2AuthorizationServer(build, dependency);
		}
		if (!hasMcpServer && !hasMcpClient && !hasAuthServer) {
			addDefaultSetup(build, dependency);
		}
	}

	private void handleMcpServer(Build build, Dependency dependency) {
		build.dependencies()
			.add("mcp-server-security", Dependency.from(dependency).artifactId("mcp-server-security").build());
		if (!build.dependencies().has("oauth2-resource-server")) {
			build.dependencies().add("security");
		}
	}

	private void handleMcpClient(Build build, Dependency dependency) {
		build.dependencies()
			.add("mcp-client-security", Dependency.from(dependency).artifactId("mcp-client-security").build());
		if (!build.dependencies().has("security")) {
			build.dependencies().add("security");
		}
	}

	private void handleOAuth2AuthorizationServer(Build build, Dependency dependency) {
		build.dependencies()
			.add("mcp-authorization-server",
					Dependency.from(dependency).artifactId("mcp-authorization-server").build());
	}

	private void addDefaultSetup(Build build, Dependency dependency) {
		build.dependencies().add("spring-ai-mcp-client");
		build.dependencies()
			.add("mcp-client-security", Dependency.from(dependency).artifactId("mcp-client-security").build());
		if (!build.dependencies().has("security")) {
			build.dependencies().add("security");
		}
	}

}
