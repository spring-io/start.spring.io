/*
 * Copyright 2012-2023 the original author or authors.
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
