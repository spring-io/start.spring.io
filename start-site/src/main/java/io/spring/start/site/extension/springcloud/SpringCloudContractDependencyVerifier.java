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

/**
 * An interface for verifying the presence of Spring Cloud Contract dependencies.
 *
 * @author Olga Maciaszek-Sharma
 */
interface SpringCloudContractDependencyVerifier {

	/**
	 * Spring Cloud Contract Verifier artifact id.
	 */
	String SPRING_CLOUD_CONTRACT_ARTIFACT_ID = "org.springframework.cloud:spring-cloud-contract-verifier";

	/**
	 * Spring Cloud Contract Verifier start-site dependency id.
	 */
	String CLOUD_CONTRACT_VERIFIER_DEPENDENCY_ID = "cloud-contract-verifier";

	/**
	 * Checks if Spring Cloud Contract Verifier is not present in provided dependency
	 * {@link Map}.
	 * @param dependencies dependency {@link Map} to search through
	 * @return <code>true</code> if Spring Cloud Contract Verifier dependency not present
	 */
	default boolean doesNotContainSCCVerifier(Map<String, Dependency> dependencies) {
		return dependencies.keySet().stream().noneMatch((id) -> id.equals(CLOUD_CONTRACT_VERIFIER_DEPENDENCY_ID));
	}

}
