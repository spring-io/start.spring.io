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

package io.spring.start.site.extension.dependency.springai;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on MCP server or client.
 *
 * @author Moritz Halbritter
 * @author Ilayaperumal Gopinathan
 */
@ProjectGenerationConfiguration
class SpringAiMcpProjectGenerationConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnRequestedDependency(SpringAiMcpServer.DEPENDENCY_ID)
	static class SpringAiMcpServer {

		static final String DEPENDENCY_ID = "spring-ai-mcp-server";

		@Bean
		@ConditionalOnRequestedDependency("web")
		BuildCustomizer<Build> springAiMcpServerWebMvc() {
			return (build) -> {
				Dependency dependency = build.dependencies().get(DEPENDENCY_ID);
				build.dependencies().remove(DEPENDENCY_ID);
				build.dependencies()
					.add(DEPENDENCY_ID, dependency.getGroupId(), "spring-ai-starter-mcp-server-webmvc",
							dependency.getScope());
			};
		}

		@Bean
		@ConditionalOnRequestedDependency("webflux")
		BuildCustomizer<Build> springAiMcpServerWebFlux() {
			return (build) -> {
				Dependency dependency = build.dependencies().get(DEPENDENCY_ID);
				build.dependencies().remove(DEPENDENCY_ID);
				build.dependencies()
					.add(DEPENDENCY_ID, dependency.getGroupId(), "spring-ai-starter-mcp-server-webflux",
							dependency.getScope());
			};
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnRequestedDependency(SpringAiMcpClient.DEPENDENCY_ID)
	static class SpringAiMcpClient {

		static final String DEPENDENCY_ID = "spring-ai-mcp-client";

		@Bean
		@ConditionalOnRequestedDependency("webflux")
		BuildCustomizer<Build> springAiMcpClientWebFlux() {
			return (build) -> {
				Dependency dependency = build.dependencies().get(DEPENDENCY_ID);
				build.dependencies().remove(DEPENDENCY_ID);
				build.dependencies()
					.add(DEPENDENCY_ID, dependency.getGroupId(), "spring-ai-starter-mcp-client-webflux",
							dependency.getScope());
			};
		}

	}

}
