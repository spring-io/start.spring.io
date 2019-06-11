/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.springcloud;

import java.util.Map;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * An util class for Spring Cloud Contract {@link BuildCustomizer} implementations.
 *
 * @author Olga Maciaszek-Sharma
 */
final class SpringCloudContractBuildCustomizerUtils {

	static final String SPRING_CLOUD_CONTRACT_ID = "spring-cloud-contract";
	static final String CLOUD_CONTRACT_VERIFIER_DEPENDENCY_ID = "cloud-contract-verifier";

	private SpringCloudContractBuildCustomizerUtils() {
		throw new AssertionError("Must not instantiate utility class.");
	}

	static boolean doesNotContainSCCVerifier(Map<String, Dependency> dependencies) {
		return dependencies.keySet().stream()
				.noneMatch((id) -> id.equals(CLOUD_CONTRACT_VERIFIER_DEPENDENCY_ID));
	}

}
