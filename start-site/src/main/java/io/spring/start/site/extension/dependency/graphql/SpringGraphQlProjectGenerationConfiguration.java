/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.dependency.graphql;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Spring for GraphQL.
 *
 * @author Andy Wilkinson
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("graphql")
class SpringGraphQlProjectGenerationConfiguration {

	@Bean
	SpringGraphQlProjectContributor SpringGraphQlProjectContributor() {
		return new SpringGraphQlProjectContributor();
	}

}
