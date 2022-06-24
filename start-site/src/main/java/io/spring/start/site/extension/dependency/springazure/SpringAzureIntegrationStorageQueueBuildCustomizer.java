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

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that follows some rules to add dependencies:<br>
 * Integration Service <br>
 * Automatically adds "spring-cloud-azure-starter-integration-storage-queue" when
 * integration and Azure Storage dependencies are selected.<br>
 *
 * @author Yonghui Ye
 */
public class SpringAzureIntegrationStorageQueueBuildCustomizer implements BuildCustomizer<Build> {

	private static final String INTEGRATION_DEPENDENCY_ID = "integration";

	private static final String AZURE_STORAGE_DEPENDENCY_ID = "azure-storage";

	@Override
	public void customize(Build build) {
		customizeIntegrationDependency(build);
	}

	private void customizeIntegrationDependency(Build build) {
		if (build.dependencies().has(INTEGRATION_DEPENDENCY_ID)
				&& build.dependencies().has(AZURE_STORAGE_DEPENDENCY_ID)) {
			build.dependencies().add("spring-cloud-azure-starter-integration-storage-queue", Dependency
					.withCoordinates("com.azure.spring", "spring-cloud-azure-starter-integration-storage-queue"));
		}
	}

}
