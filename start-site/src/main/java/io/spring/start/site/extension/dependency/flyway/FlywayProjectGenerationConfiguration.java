/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.dependency.flyway;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Flyway.
 *
 * @author Stephane Nicoll
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("flyway")
class FlywayProjectGenerationConfiguration {

	@Bean
	FlywayProjectContributor flywayProjectContributor() {
		return new FlywayProjectContributor();
	}

	@Bean
	FlywayBuildCustomizer flywayBuildCustomizer() {
		return new FlywayBuildCustomizer();
	}

}
