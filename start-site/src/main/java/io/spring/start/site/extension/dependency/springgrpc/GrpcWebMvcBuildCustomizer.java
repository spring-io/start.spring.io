/*
 * Copyright 2012 - present the original author or authors.
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

package io.spring.start.site.extension.dependency.springgrpc;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to replace 'spring-grpc-spring-boot-starter' with
 * 'spring-grpc-server-web-spring-boot-starter' if WebMVC is selected.
 *
 * @author Moritz Halbritter
 */
class GrpcWebMvcBuildCustomizer implements BuildCustomizer<Build> {

	private static final String DEPENDENCY_ID = "spring-grpc";

	@Override
	public void customize(Build build) {
		Dependency dependency = build.dependencies().get(DEPENDENCY_ID);
		build.dependencies().remove(DEPENDENCY_ID);
		build.dependencies()
			.add(DEPENDENCY_ID, dependency.getGroupId(), "spring-grpc-server-web-spring-boot-starter",
					dependency.getScope());
	}

}
