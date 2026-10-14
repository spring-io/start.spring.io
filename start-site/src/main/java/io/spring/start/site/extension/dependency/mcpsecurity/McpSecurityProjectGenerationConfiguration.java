/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.mcpsecurity;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

import org.springframework.context.annotation.Bean;

/**
 * {@link ProjectGenerationConfiguration} for projects that depend on MCP security.
 *
 * @author Moritz Halbritter
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("mcp-security")
class McpSecurityProjectGenerationConfiguration {

	@Bean
	McpSecurityBuildCustomizer mcpSecurityBuildCustomizer() {
		return new McpSecurityBuildCustomizer();
	}

}
