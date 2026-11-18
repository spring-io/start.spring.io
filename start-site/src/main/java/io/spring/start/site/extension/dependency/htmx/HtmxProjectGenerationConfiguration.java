/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.dependency.htmx;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on htmx.
 *
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("htmx")
class HtmxProjectGenerationConfiguration {

	@Bean
	HtmxBuildCustomizer htmxBuildCustomizer() {
		return new HtmxBuildCustomizer();
	}

}
