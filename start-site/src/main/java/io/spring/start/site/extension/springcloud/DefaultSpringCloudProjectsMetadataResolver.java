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

import java.util.Optional;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * Default implementation of the {@link SpringCloudProjectsMetadataResolver}.
 *
 * @author Olga Maciaszek-Sharma
 */
class DefaultSpringCloudProjectsMetadataResolver
		implements SpringCloudProjectsMetadataResolver {

	private static final String SPRING_CLOUD_ID = "spring-cloud";

	private final InitializrMetadata metadata;

	private final SpringCloudProjectsMetadataProvider metadataProvider;

	DefaultSpringCloudProjectsMetadataResolver(InitializrMetadata metadata,
			SpringCloudProjectsMetadataProvider metadataProvider) {
		this.metadata = metadata;
		this.metadataProvider = metadataProvider;
	}

	@Override
	public Optional<String> resolveProjectVersion(Version bootVersion, String projectId) {
		BillOfMaterials bom = this.metadata.getConfiguration().getEnv().getBoms()
				.get(SPRING_CLOUD_ID);
		String releaseTrainVersion = bom.resolve(bootVersion).getVersion();
		SpringCloudProjectsMetadata springCloudProjectsMetadata = this.metadataProvider
				.get(releaseTrainVersion);
		if (springCloudProjectsMetadata != null) {
			return Optional
					.ofNullable(springCloudProjectsMetadata.getProjects().get(projectId));
		}
		return Optional.empty();
	}

}
