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
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * A {@link ProjectDescriptionCustomizer} that checks the web stack that is used with
 * Spring Cloud Gateway as it requires a dedicated starter. Before Spring Boot 3.2, MVC
 * was not supported.
 *
 * @author Stephane Nicoll
 */
public class SpringCloudGatewayProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private static final VersionRange SPRING_BOOT_3_2_0_OR_LATER = VersionParser.DEFAULT.parseRange("3.2.0-M1");

	@Override
	public void customize(MutableProjectDescription description) {
		Collection<String> dependencyIds = description.getRequestedDependencies().keySet();
		if (!dependencyIds.contains("cloud-gateway")) {
			return;
		}
		if (SPRING_BOOT_3_2_0_OR_LATER.match(description.getPlatformVersion())) {
			swapStarterIfNecessary(description, dependencyIds);
		}
		else if (dependencyIds.contains("web")) {
			description.removeDependency("web");
			if (!description.getRequestedDependencies().containsKey("webflux")) {
				description.addDependency("webflux",
						Dependency.withCoordinates("org.springframework.boot", "spring-boot-starter-webflux"));
			}
		}
	}

	private void swapStarterIfNecessary(MutableProjectDescription description, Collection<String> dependencyIds) {
		if (dependencyIds.contains("web")) {
			description.addDependency("cloud-gateway",
					Dependency.from(description.getRequestedDependencies().get("cloud-gateway"))
						.artifactId("spring-cloud-starter-gateway-mvc"));
		}

	}

}
