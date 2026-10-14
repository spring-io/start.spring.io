/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringCloudCircuitBreakerBuildCustomizer}.
 *
 * @author Madhura Bhave
 */
class SpringCloudCircuitBreakerBuildCustomizerTests extends AbstractExtensionTests {

	private static final Dependency REACTIVE_CLOUD_CIRCUIT_BREAKER = Dependency.withId("cloud-resilience4j-reactive",
			"org.springframework.cloud", "spring-cloud-starter-circuitbreaker-reactor-resilience4j");

	@Test
	void replacesCircuitBreakerWithReactiveCircuitBreakerWhenReactiveFacetPresent() {
		ProjectRequest request = createProjectRequest("webflux", "cloud-resilience4j");
		assertThat(mavenPom(request)).hasDependency(getDependency("webflux"))
			.hasDependency(REACTIVE_CLOUD_CIRCUIT_BREAKER)
			.doesNotHaveDependency("org.springframework.cloud", "spring-cloud-starter-circuitbreaker-resilience4j")
			.hasBom("org.springframework.cloud", "spring-cloud-dependencies", "${spring-cloud.version}")
			.hasBomsSize(1);
	}

	@Test
	void doesNotReplaceCircuitBreakerWithReactiveCircuitBreakerWhenReactiveFacetNotPresent() {
		ProjectRequest request = createProjectRequest("cloud-resilience4j");
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-resilience4j"))
			.doesNotHaveDependency("org.springframework.cloud",
					"spring-cloud-starter-circuitbreaker-reactor-resilience4j")
			.hasBom("org.springframework.cloud", "spring-cloud-dependencies", "${spring-cloud.version}")
			.hasBomsSize(1);
	}

}
