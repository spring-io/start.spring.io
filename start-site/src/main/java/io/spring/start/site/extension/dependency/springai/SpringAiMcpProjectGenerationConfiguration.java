/*
 * Copyright 2012 - present the original author or authors.
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
