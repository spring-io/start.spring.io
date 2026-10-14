/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.project.dependency.springcloud;

import java.util.Collection;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;

/**
 * A {@link ProjectDescriptionCustomizer} that checks the web stack that is used with
 * Spring Cloud Resilience4j as it has a starter for the servlet stack and another one for
 * the reactive stack.
 *
 * @author Stephane Nicoll
 */
public class SpringCloudResilience4JProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	@Override
	public void customize(MutableProjectDescription description) {
		Collection<String> dependencyIds = description.getRequestedDependencies().keySet();
		if (!dependencyIds.contains("cloud-resilience4j")) {
			return;
		}
		if (dependencyIds.contains("webflux")) {
			description.addDependency("cloud-resilience4j",
					Dependency.from(description.getRequestedDependencies().get("cloud-resilience4j"))
						.artifactId("spring-cloud-starter-circuitbreaker-reactor-resilience4j"));
		}
	}

}
