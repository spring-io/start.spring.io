/*
 * Copyright 2012-2021 the original author or authors.
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

	static final Dependency REACTIVE_CLOUD_CIRCUIT_BREAKER = Dependency.withId("cloud-resilience4j-reactive",
			"org.springframework.cloud", "spring-cloud-starter-circuitbreaker-reactor-resilience4j");

	@Test
	void replacesCircuitBreakerWithReactiveCircuitBreakerWhenReactiveFacetPresent() {
		ProjectRequest request = createProjectRequest("webflux", "cloud-resilience4j");
		request.setBootVersion("2.5.0");
		assertThat(mavenPom(request)).hasDependency(getDependency("webflux"))
				.hasDependency(REACTIVE_CLOUD_CIRCUIT_BREAKER)
				.doesNotHaveDependency("org.springframework.cloud", "spring-cloud-starter-circuitbreaker-resilience4j")
				.hasBom("org.springframework.cloud", "spring-cloud-dependencies", "${spring-cloud.version}")
				.hasBomsSize(1);
	}

	@Test
	void doesNotReplaceCircuitBreakerWithReactiveCircuitBreakerWhenReactiveFacetNotPresent() {
		ProjectRequest request = createProjectRequest("cloud-resilience4j");
		request.setBootVersion("2.5.0");
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-resilience4j"))
				.doesNotHaveDependency("org.springframework.cloud",
						"spring-cloud-starter-circuitbreaker-reactor-resilience4j")
				.hasBom("org.springframework.cloud", "spring-cloud-dependencies", "${spring-cloud.version}")
				.hasBomsSize(1);
	}

}
