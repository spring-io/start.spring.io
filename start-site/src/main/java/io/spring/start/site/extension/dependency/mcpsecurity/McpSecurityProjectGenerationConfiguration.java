/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
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
