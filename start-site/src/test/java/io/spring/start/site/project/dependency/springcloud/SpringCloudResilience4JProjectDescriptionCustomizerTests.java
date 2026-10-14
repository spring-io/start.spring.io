/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.project.dependency.springcloud;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.MutableProjectDescription;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SpringCloudResilience4JProjectDescriptionCustomizer}.
 *
 * @author Stephane Nicoll
 */
class SpringCloudResilience4JProjectDescriptionCustomizerTests {

	@Test
	void customizeWithSpringCloudResilience4jAndSpringMvcUsesMvcStarter() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.addDependency("cloud-resilience4j", Dependency.withCoordinates("org.springframework.cloud",
				"spring-cloud-starter-circuitbreaker-resilience4j"));
		description.addDependency("web", mock(Dependency.class));
		new SpringCloudResilience4JProjectDescriptionCustomizer().customize(description);
		assertThat(description.getRequestedDependencies()).containsOnlyKeys("cloud-resilience4j", "web");
		Dependency cloudResilience4j = description.getRequestedDependencies().get("cloud-resilience4j");
		assertThat(cloudResilience4j.getArtifactId()).isEqualTo("spring-cloud-starter-circuitbreaker-resilience4j");
	}

	@Test
	void customizeWithSpringCloudResilience4jAndSpringWebfluxUsesReactiveStarter() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.addDependency("cloud-resilience4j", Dependency.withCoordinates("org.springframework.cloud",
				"spring-cloud-starter-circuitbreaker-resilience4j"));
		description.addDependency("webflux", mock(Dependency.class));
		new SpringCloudResilience4JProjectDescriptionCustomizer().customize(description);
		assertThat(description.getRequestedDependencies()).containsOnlyKeys("cloud-resilience4j", "webflux");
		Dependency cloudResilience4j = description.getRequestedDependencies().get("cloud-resilience4j");
		assertThat(cloudResilience4j.getArtifactId())
			.isEqualTo("spring-cloud-starter-circuitbreaker-reactor-resilience4j");
	}

}
