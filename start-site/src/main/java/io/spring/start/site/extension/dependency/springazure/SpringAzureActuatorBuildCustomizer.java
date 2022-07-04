/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that automatically adds "spring-cloud-azure-starter-actuator"
 * when any Spring Cloud Azure library and Spring Boot Actuator are selected.
 *
 * @author Yonghui Ye
 */
class SpringAzureActuatorBuildCustomizer implements BuildCustomizer<Build> {

	private static final String ACTUATOR_DEPENDENCY_ID = "actuator";

	@Override
	public void customize(Build build) {
		if (build.dependencies().items().anyMatch((u) -> u.getGroupId().equals("com.azure.spring"))
				&& build.dependencies().has(ACTUATOR_DEPENDENCY_ID)) {
			build.dependencies().add("spring-cloud-azure-starter-actuator",
					Dependency.withCoordinates("com.azure.spring", "spring-cloud-azure-starter-actuator"));
		}
	}

}
