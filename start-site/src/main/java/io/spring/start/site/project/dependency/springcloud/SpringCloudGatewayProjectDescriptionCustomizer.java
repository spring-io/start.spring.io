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

package io.spring.start.site.project.dependency.springcloud;

import java.util.Collection;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;

/**
 * A {@link ProjectDescriptionCustomizer} that checks that Spring Cloud Gateway is not
 * used with Spring MVC as only Spring WebFlux is supported.
 *
 * @author Stephane Nicoll
 */
public class SpringCloudGatewayProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	@Override
	public void customize(MutableProjectDescription description) {
		Collection<String> dependencyIds = description.getRequestedDependencies().keySet();
		if (dependencyIds.contains("cloud-gateway") && dependencyIds.contains("web")) {
			description.removeDependency("web");
			if (!description.getRequestedDependencies().containsKey("webflux")) {
				description.addDependency("webflux",
						Dependency.withCoordinates("org.springframework.boot", "spring-boot-starter-webflux"));
			}
		}
	}

}
